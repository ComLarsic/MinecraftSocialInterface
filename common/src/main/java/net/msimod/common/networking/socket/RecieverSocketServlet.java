package net.msimod.common.networking.socket;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * The servlet that handles the socket.
 * 
 */
@WebServlet
public class RecieverSocketServlet extends WebSocketHandler {
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(RecieverSocket.class);
    }
}
