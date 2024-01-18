package net.msimod.common;

import java.io.Serializable;

/**
 * The mod config.
 * Must be serializable to JSON.
 */
public class MsiModConfig implements Serializable {
    /**
     * The server config.
     */
    public static class Server implements Serializable {
        public int port = 8080;
    }

    /**
     * The microsoft auth config.
     */
    public static class MicrosoftAuth implements Serializable {
        public String clientId = "";
        public String clientSecret = "";
    }

    public String postServer = "http://localhost:5014";
    public Server server = new Server();
    public MicrosoftAuth microsoftAuth = new MicrosoftAuth();
}
