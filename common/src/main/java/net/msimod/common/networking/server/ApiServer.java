package net.msimod.common.networking.server;

import net.msimod.common.MsiMod;
import net.msimod.common.networking.servlet.AuthRedirectServlet;
import net.msimod.common.networking.servlet.PlayerServlet;
import net.msimod.common.networking.servlet.ProfileServlet;
import net.msimod.common.networking.socket.RecieverSocket;
import net.msimod.common.networking.socket.Message;
import net.msimod.common.networking.socket.RecieverSocketServlet;
import net.msimod.common.networking.socket.auth.AuthMessageHandler;
import net.msimod.common.networking.socket.chat.ChatMessageHandler;
import net.msimod.common.networking.servlet.ChatLogServlet;
import net.msimod.common.networking.webclient.WebClientServlet;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

/**
 * The server that provides the openings for the api
 */
public class ApiServer implements AutoCloseable {
    private Server server;

    /**
     * Start the server
     * 
     * @throws Exception
     */
    public void start() throws Exception {
        server = new Server(MsiMod.CONFIG.server.port);
        var handlers = new HandlerCollection();
        setupServlets(handlers);
        setupSockets(handlers);
        server.setHandler(handlers);
        server.start();
    }

    /**
     * Stop the server
     * 
     * @throws Exception
     */
    public void stop() throws Exception {
        MsiMod.MINECRAFT_SERVER.getPlayerList().getOps().remove(MsiMod.SERVER_PLAYER.getGameProfile());
        server.stop();
    }

    @Override
    public void close() throws Exception {
        stop(); // Stop the server when the object is disposed
    }

    /**
     * Setup the servlets
     * 
     * @param handlers The handler collection to add the servlets to
     */
    private void setupServlets(HandlerCollection handlerCollection) {
        // Setup CORS
        var filterHolder = new FilterHolder(new CrossOriginFilter());
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*"); // Allow all origins
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD"); // Allow GET, POST and
                                                                                                 // HEAD methodss
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "*"); // Allow all headers
        filterHolder.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true"); // Allow credentials
        filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*"); // Allow all origins

        // Add the servlets
        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(ProfileServlet.class, "/api/profiles/*");
        context.addServlet(PlayerServlet.class, "/api/players/*");
        context.addServlet(ChatLogServlet.class, "/api/chat/log/*");
        // The auth redirect servlet
        context.addServlet(AuthRedirectServlet.class, "/auth/microsoft");
        // The web client
        context.addServlet(WebClientServlet.class, "/");
        context.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));
        handlerCollection.addHandler(context);
    }

    /**
     * Setup the socket endpoint
     * 
     * @param handlers The handler collection to add the socket endpoint to
     */
    private void setupSockets(HandlerCollection handlers) {
        // Setup CORS
        var filterHolder = new FilterHolder(new CrossOriginFilter());
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*"); // Allow all origins

        // Add the socket endpoint
        RecieverSocket.registerHandler(Message.Handlers.AUTH, new AuthMessageHandler());
        RecieverSocket.registerHandler(Message.Handlers.CHAT, new ChatMessageHandler());

        var socketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        socketContext.setContextPath("/ws/socket");
        var recieverSocket = new RecieverSocketServlet();
        recieverSocket.setHandler(socketContext);
        socketContext.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));
        handlers.addHandler(recieverSocket);
    }

}
