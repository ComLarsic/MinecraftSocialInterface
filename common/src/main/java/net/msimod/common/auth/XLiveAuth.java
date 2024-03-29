package net.msimod.common.auth;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.Fields;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import net.msimod.common.MsiMod;

/**
 * The class that handles authentication with the xbox live api.
 */
public class XLiveAuth {
    private static final String AUTH_URL = "https://user.auth.xboxlive.com/user/authenticate";
    private static final String AUTH_RELAYING_PARTY = "http://auth.xboxlive.com";
    private static final String AUTH_SITE_NAME = "user.auth.xboxlive.com";
    private static final String XBOX_LIVE_URL = "https://xsts.auth.xboxlive.com/xsts/authorize";
    private static final String XBOX_LIVE_RELAYING_PARTY = "http://xboxlive.com";

    /**
     * Authenticate via the microsoft access token and retrieve the user token
     */
    public static String getXliveUserToken(String accessToken) {
        try {
            var client = new HttpClient(new SslContextFactory.Client(false));
            client.start();

            var json = new StringBuilder();
            json.append("{");
            json.append("\"RelyingParty\": \"").append(AUTH_RELAYING_PARTY).append("\",");
            json.append("\"TokenType\": \"JWT\",");
            json.append("\"Properties\": {");
            json.append("\"AuthMethod\": \"RPS\",");
            json.append("\"SiteName\": \"").append(AUTH_SITE_NAME).append("\",");
            json.append("\"RpsTicket\": \"d=").append(accessToken).append("\"");
            json.append("}");
            json.append("}");

            var request = client.POST(AUTH_URL)
                    .accept("application/json")
                    .header("x-xbl-contract-version", "1")
                    .content(new StringContentProvider(json.toString()), "application/json");
            var response = request.send();
            var responseKJson = response.getContentAsString();
            var responseMap = new Gson().fromJson(responseKJson, HashMap.class);
            var token = (String) responseMap.get("Token");
            return token;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Authenticate via the user token and retrieve the final X token
     */
    public static String getXLiveToken(String userToken) {
        try {
            var client = new HttpClient(new SslContextFactory.Client(false));
            client.start();

            var json = new StringBuilder();
            json.append("{");
            json.append("\"RelyingParty\": \"").append(XBOX_LIVE_RELAYING_PARTY).append("\",");
            json.append("\"TokenType\": \"JWT\",");
            json.append("\"Properties\": {");
            json.append("\"SandboxId\": \"RETAIL\",");
            json.append("\"UserTokens\": [\"").append(userToken).append("\"]");
            json.append("}");
            json.append("}");

            var request = client.POST(XBOX_LIVE_URL)
                    .accept("application/json")
                    .header("x-xbl-contract-version", "1")
                    .content(new StringContentProvider(json.toString()), "application/json");
            var response = request.send();
            var jsonResponse = response.getContentAsString();
            return jsonResponse;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the access token from the token
     * 
     * @param token
     * @return
     * @throws ClassCastException
     */
    public static String GetAccessToken(String token) throws JsonSyntaxException {
        var tokenDecoded = new String(Base64.getDecoder().decode(token));
        var tokenMap = new Gson().fromJson(tokenDecoded, HashMap.class);
        var tokenBearer = (String) tokenMap.get("Token");
        var xui = GetXui(token);
        var userhash = (String) xui.get("uhs");
        return "XBL3.0 x=" + userhash + ";" + tokenBearer;
    }

    /**
     * Get a xui from a token
     * 
     * @param token
     * @return
     * @throws ClassCastException
     */
    public static LinkedTreeMap<Object, Object> GetXui(String token) throws ClassCastException {
        var tokenDecoded = new String(Base64.getDecoder().decode(token));
        var tokenMap = new Gson().fromJson(tokenDecoded, HashMap.class);
        var claims = LinkedTreeMap.class.cast(tokenMap.get("DisplayClaims"));
        var list = ArrayList.class.cast(claims.get("xui"));
        return LinkedTreeMap.class.cast(list.get(0));
    }
}
