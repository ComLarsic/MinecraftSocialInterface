package net.msimod.common.networking.socket;

import java.io.Serializable;

/**
 * A message that can be sent over the socket.
 */
public class Message implements Serializable {
    /**
     * The handlers for the socket
     */
    public static enum Handlers {
        NONE,
        AUTH,
        CHAT;
    }

    /**
     * The type of the message
     */
    public static enum Type {
        REGISTER,
        UPDATE,
        UNREGISTER,
        FAILURE,
        UNAUTHORIZED,
    }

    public Message(Handlers handler, Type type, Object data) {
        this.handler = handler;
        this.type = type;
        this.data = data;
    }

    public Handlers handler;
    public Type type;
    public String accessToken;
    public Object data;
}
