package net.msimod.common.networking.socket;

import java.io.Serializable;

import org.eclipse.jetty.websocket.api.Session;

/**
 * The socket recieves messages from the client and delegates them to the
 * correct handler.
 */
public interface MessageHandler {

    /**
     * Returns true if the message handler requires authentication
     * 
     * @return If the message handler requires authentication
     */
    public default boolean auhtenticate() {
        return false;
    }

    /**
     * Register a session
     * 
     * @param session The session to register
     */
    public default void register(Session session, Message message) {
    }

    /**
     * Unregister a session
     * 
     * @param session The session to unregister
     */
    public default void unregister(Session session, Message message) {
    }

    /**
     * Handle a message from the client
     * 
     * @param session The session the message came from
     * @param message The message
     */
    public default void update(Session session, Message message) {
    }
}
