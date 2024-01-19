package net.msimod.common.networking.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.msimod.common.MsiMod;
import net.msimod.common.auth.Auth;

/**
 * The servlet that serves the profile page.
 */
public class ProfileServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!Auth.validateToken(request, response)) {
            return;
        }

        // Read parameters
        var id = request.getParameter("id");

        var client = HttpClient.newBuilder().build();
        var profileRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(MsiMod.CONFIG.postServer + "/api/profile/" + id))
                .build();
        try {
            HttpResponse<String> profileResponse = client.send(profileRequest, BodyHandlers.ofString());
            if (profileResponse.statusCode() != 200) {
                MsiMod.LOGGER.error("Failed to contact post server to get profile");
                response.sendError(500);
                return;
            }
            response.getWriter().write(profileResponse.body());
        } catch (InterruptedException e) {
            MsiMod.LOGGER.error("Failed to contact post server to get profile");
            response.sendError(500);
            return;
        }
    }
}
