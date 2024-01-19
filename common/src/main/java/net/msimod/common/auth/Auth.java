package net.msimod.common.auth;

import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.auth0.jwt.JWT;
import com.google.common.base.Verify;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.SignedJWT;

import net.msimod.common.MsiMod;
import net.msimod.common.networking.socket.Message;

/**
 * The class that handles authentication.
 * Handles the response from the microsoft auth server.
 */
public class Auth {
    /// The url to the jwk provider
    public final static String JWK_PROVIDER = "https://login.microsoftonline.com/consumers/discovery/v2.0/keys";
    /// The url to request as token verification
    private final static String VERIFY_URL = "https://msg.xboxlive.com/users/";

    /**
     * Validate a request's token.
     * 
     * @param request
     * @return Whether the token is valid.
     */
    public static boolean validateToken(HttpServletRequest request, HttpServletResponse response) {
        var token = request.getHeader("Authorization");
        // Strip the "Bearer " prefix
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);

        var valid = validateToken(token);
        if (!valid)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return valid;
    }

    /**
     * Validate a socket message's token.
     * return response.getStatus() == 200;
     * 
     * @param message
     * @return Whether the token is valid.
     */
    public static boolean validateToken(Message message) {
        var token = message.accessToken;
        var valid = validateToken(token);
        return valid;
    }

    /**
     * Validates the given token.
     * 
     * @param token The token to validate.
     * @return Whether the token is valid.
     */
    public static boolean validateToken(String token) {
        try {
            var accessToken = XLiveAuth.GetAccessToken(token);
            var xui = XLiveAuth.GetXui(token);
            var xuid = (String) xui.get("xid");
            var client = new HttpClient(new SslContextFactory.Client(false));
            client.start();
            var uri = VERIFY_URL + "xuid(" + xuid + ")/inbox";
            var request = client.newRequest(uri)
                    .method("GET")
                    .header("Authorization", accessToken);
            var response = request.send();
            return response.getStatus() == 200;
        } catch (Exception e) {
            MsiMod.LOGGER.error("Failed to validate token: {}", e);
            return false;
        }
    }
}
