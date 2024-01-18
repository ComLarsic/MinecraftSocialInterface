package net.msimod.common.networking.socket;

import java.util.HashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;

import net.msimod.common.MsiMod;
import net.msimod.common.auth.Auth;

/**
 * The socket recieves messages from the client and delegates them to the
 * correct handler.
 */
@WebSocket
public class RecieverSocket {
    /// The handlers for the socket
    private static HashMap<Message.Handlers, MessageHandler> _handlers = new HashMap<Message.Handlers, MessageHandler>();

    /**
     * Register a handler for a message type
     * 
     * @param handler The handler
     */
    public static void registerHandler(Message.Handlers handler, MessageHandler messageHandler) {
        _handlers.put(handler, messageHandler);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        MsiMod.LOGGER.info("Socket connected: {}", session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        var gson = new Gson();

        try {
            // Parse the message
            var msg = gson.fromJson(message, Message.class);
            var handler = _handlers.get(msg.handler);
            if (handler == null)
                return;

            // Authenticate the user if required
            if (handler.auhtenticate()) {
                if (!Auth.validateToken(message)) {
                    // Send unauthorized message
                    var json = gson.toJson(new Message(Message.Handlers.NONE, Message.Type.UNAUTHORIZED, null));
                    session.getRemote().sendString(json);
                    return;
                }
            }

            // Handle the message
            handle(handler, session, msg);
        } catch (Exception e) {
            MsiMod.LOGGER.error("Failed to parse message: {}", e);
            return;
        }
    }

    @OnWebSocketError
    public void onError(Throwable error) {
        MsiMod.LOGGER.error("Socket error: {}", error);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        MsiMod.LOGGER.info("Socket closed: {} {}", statusCode, reason);
    }

    /**
     * Handle a message from the client
     * 
     * @param session The session the message came from
     * @param message The message
     */
    private void handle(MessageHandler handler, Session session, Message message) {
        switch (message.type) {
            case REGISTER:
                handler.register(session);
                break;
            case UPDATE:
                handler.update(session, message.data);
                break;
            case UNREGISTER:
                handler.unregister(session);
                break;
            default:
                break;
        }
    }
}
