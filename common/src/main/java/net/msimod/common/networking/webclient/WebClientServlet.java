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
            var htmlPage = getClass().getClassLoader().getResourceAsStream("web/index.html");
            var bundleJs = getClass().getClassLoader().getResourceAsStream("web/bundle.js");
            assert bundleJs != null;
            assert htmlPage != null;
            var bundleJsString = new String(bundleJs.readAllBytes());
            var htmlString = new String(htmlPage.readAllBytes());
            return htmlString.replace("#script#", bundleJsString);
        } catch (IOException e) {
            MsiMod.LOGGER.error("Failed to load bundle.js");
            return "Failed to load bundle.js";
        }
    }
}
