package net.msimod.common.networking.server;

import net.msimod.common.networking.servlet.ChatServlet;
import net.msimod.common.networking.servlet.PlayerServlet;
import net.msimod.common.networking.servlet.ChatLogServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * The server that provides the openings for the api
 */
public class ApiServer implements AutoCloseable {
    private Server server;
    private final ServletHandler handler = new ServletHandler();

    public void start() throws Exception {
        server = new Server(8080);
        handler.addServletWithMapping(PlayerServlet.class, "/players/*");
        handler.addServletWithMapping(ChatServlet.class, "/chat/send/*");
        handler.addServletWithMapping(ChatLogServlet.class, "/chat/log/*");
        server.setHandler(handler);
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    @Override
    public void close() throws Exception {
        stop();
    }
}
