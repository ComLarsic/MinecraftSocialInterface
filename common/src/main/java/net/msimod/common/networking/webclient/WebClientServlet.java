package net.msimod.common.networking.webclient;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.msimod.common.MsiMod;

import java.io.IOException;

/**
 * The servlet that serves the web client.
 * This is the entry point for the web client.
 */
public class WebClientServlet extends HttpServlet {
    public static final String HTML_PAGE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Minecraft Social Interface</title>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "    <app-root name=\"World\"></app-root>\n" +
            "    <script type=\"module\">\n" +
            "       #script#\n" +
            "    </script>\n" +
            "</body>" +
            "</html>";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(HtmlPage());
    }

    /**
     * Load the html page
     * 
     * @return The html page
     */
    private String HtmlPage() {
        // Load web/bundle.js
        try {
            var bundleJs = getClass().getClassLoader().getResourceAsStream("web/bundle.js");
            assert bundleJs != null;
            var bundleJsString = new String(bundleJs.readAllBytes());
            return HTML_PAGE.replace("#script#", bundleJsString);
        } catch (IOException e) {
            MsiMod.LOGGER.error("Failed to load bundle.js");
            return "Failed to load bundle.js";
        }
    }
}
