package net.msimod.common.networking.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.Fields;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.google.gson.Gson;

import net.msimod.common.MsiMod;
import net.msimod.common.auth.XLiveAuth;
import net.msimod.common.networking.socket.auth.AuthMessageHandler;

/**
 * The servlet that handles authentication.
 * Handles the response from the microsoft auth server.
 */
@WebServlet
public class AuthRedirectServlet extends HttpServlet {
    private static final String AUTH_URL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var code = request.getParameter("code");
        var state = request.getParameter("state");
        var sessionId = UUID.fromString(state);
        if (code == null || sessionId == null) {
            AuthMessageHandler.sendAuthFailedToClient(sessionId, 404);
            return;
        }
        var accessToken = getAccessToken(code);
        var xUserToken = XLiveAuth.getXliveUserToken(accessToken);
        var xToken = XLiveAuth.getXLiveToken(xUserToken);
        if (xToken == null) {
            AuthMessageHandler.sendAuthFailedToClient(sessionId, 500);
            return;
        }
        AuthMessageHandler.sendAccessTokenToClient(sessionId, xToken);
        response.sendRedirect("/?close=true");
    }

    /**
     * Get the access token
     * 
     * @param code The code to exchange for an access token
     */
    private static String getAccessToken(String code) {
        var params = getAuthParams(code);

        try {
            var client = new HttpClient(new SslContextFactory.Client(false));
            client.start();
            var request = client.newRequest(AUTH_URL)
                    .accept("application/json")
                    .content(params)
                    .method("POST");
            var response = request.send();
            var responseContent = response.getContentAsString();
            var gson = new Gson();
            var responseJson = gson.fromJson(responseContent, HashMap.class);
            var accessToken = (String) responseJson.get("access_token");
            return accessToken;
        } catch (Exception e) {
            MsiMod.LOGGER.error("Failed to get access token: {}", e);
            return null;
        }
    }

    /**
     * Create the auth parameters
     * 
     * @param code The code to exchange for an access token
     * @return The auth paramaters
     */
    private static FormContentProvider getAuthParams(String code) {
        var config = MsiMod.CONFIG;
        var params = new Fields();
        params.put("client_id", config.microsoftAuth.clientId);
        params.put("client_secret", config.microsoftAuth.clientSecret);
        params.put("grant_type", "authorization_code");
        params.put("scope", "Xboxlive.signin Xboxlive.offline_access");
        params.put("code", code);
        params.put("redirect_uri", "http://localhost:" + config.server.port + "/auth/microsoft");
        return new FormContentProvider(params);
    }
}
