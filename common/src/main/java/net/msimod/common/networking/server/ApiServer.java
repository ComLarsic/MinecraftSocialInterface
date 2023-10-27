package net.msimod.common.networking.server;

import net.minecraft.server.MinecraftServer;
import net.msimod.common.MsiMod;
import net.msimod.common.networking.servlet.ChatServlet;
import net.msimod.common.networking.servlet.PlayerServlet;
import net.msimod.common.networking.servlet.ChatLogServlet;
import net.msimod.common.networking.webclient.WebClientServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * The server that provides the openings for the api
 */
public class ApiServer implements AutoCloseable {
    private Server server;
    private final ServletHandler handler = new ServletHandler();

    public void start() throws Exception {
        var context = new ServletContextHandler();

        server = new Server(8080);
        context.addServlet(PlayerServlet.class, "/api/players/*");
        context.addServlet(ChatServlet.class, "/api/chat/send/*");
        context.addServlet(ChatLogServlet.class, "/api/chat/log/*");
        context.addServlet(WebClientServlet.class, "/");

        handler.setHandler(context);
        server.setHandler(handler);
        server.start();
    }

    public void stop() throws Exception {
        MsiMod.MINECRAFT_SERVER.getPlayerList().getOps().remove(MsiMod.SERVER_PLAYER.getGameProfile());
        server.stop();
    }

    @Override
    public void close() throws Exception {
        stop();
    }
}
