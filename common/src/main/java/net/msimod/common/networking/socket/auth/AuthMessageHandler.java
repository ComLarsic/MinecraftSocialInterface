package net.msimod.common.networking.socket.auth;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

import net.msimod.common.MsiMod;
import net.msimod.common.networking.socket.Message;
import net.msimod.common.networking.socket.MessageHandler;

/**
 * The socket for the auth functionality.
 * Handles the authentication of the user.
 * 
 * The pipeline is as follows:
 * 1. The client connects to the server
 * 2. The server sends the auth url to the client
 * 3. The client opens the url in a browser
 * 4. The user logs in
 * 5. The user is redirected to the server
 * 6. The server handles the response from the auth server
 * 7. The server sends the access token to the client
 */
public class AuthMessageHandler implements MessageHandler {
    private static final String AUTH_URL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize";

    /// The clients connected to the server
    private static HashMap<UUID, Session> _linkedClients = new HashMap<UUID, Session>();

    /**
     * Send the access token to the client
     * 
     * @param sessionId   The session id
     * @param accessToken The access token
     */
    public static void sendAccessTokenToClient(UUID sessionId, String accessToken) {
        var session = _linkedClients.get(sessionId);
        var gson = new Gson();
        var json = gson.toJson(new Message(Message.Handlers.AUTH, Message.Type.UPDATE, accessToken));
        if (session == null)
            return;
        try {
            session.getRemote().sendString(json);
        } catch (Exception e) {
            MsiMod.LOGGER.error("Failed to send access token to client: {}", e);
            _linkedClients.remove(sessionId);
        }
    }

    /**
     * Tell the client that the auth failed
     * 
     * @param sessionId The session id
     * @param status    The status code
     */
    public static void sendAuthFailedToClient(UUID sessionId, int status) {
        var session = _linkedClients.get(sessionId);
        if (session == null)
            return;
        try {
            var message = new Message(Message.Handlers.AUTH, Message.Type.FAILURE, "authFailed: " + status);
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (Exception e) {
            MsiMod.LOGGER.error("Failed to send auth failed to client: {}", e);
            _linkedClients.remove(sessionId);
        }
    }

    @Override
    public void register(Session session) {
        var sessionId = UUID.randomUUID();
        var url = CreateAuthUrl(sessionId);
        var gson = new Gson();
        var message = new Message(Message.Handlers.AUTH, Message.Type.UPDATE, url);
        var json = gson.toJson(message);
        MsiMod.LOGGER.info("Sending auth url to client: {}", json);
        try {
            session.getRemote().sendString(json);
            _linkedClients.put(sessionId, session);
        } catch (Exception e) {
            MsiMod.LOGGER.error("Failed to send auth url to client: {}", e);
            return;
        }
    }

    @Override
    public void unregister(Session session) {
        _linkedClients.values().remove(session);
    }

    /**
     * Create the auth url
     * 
     * @param sessionId
     * @return The auth url
     */
    private static String CreateAuthUrl(UUID sessionId) {
        var config = MsiMod.CONFIG;
        var url = AUTH_URL
                + "?client_id=" + config.microsoftAuth.clientId
                + "&response_type=code"
                + "&redirect_uri=http://localhost:" + config.server.port + "/auth/microsoft"
                + "&approval_propmt=auto"
                + "&scope=Xboxlive.signin Xboxlive.offline_access"
                + "&state=" + sessionId.toString();
        return url;
    }
}
