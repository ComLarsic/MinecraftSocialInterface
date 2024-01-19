package net.msimod.common.networking.webclient;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.msimod.common.MsiMod;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

/**
 * The servlet that serves the web client.
 * This is the entry point for the web client.
 */
public class WebClientServlet extends HttpServlet {
    private static String HTML;

    public WebClientServlet() {
        super();
        HTML = HtmlPage();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(HTML);
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
            // var fontwolf =
            // getClass().getClassLoader().getResourceAsStream("web/assets/fonts/minecraftregular.woff");
            // var fontwolfTwo = getClass().getClassLoader()
            // .getResourceAsStream("web/assets/fonts/minecraftregular.woff2");
            assert bundleJs != null;
            assert htmlPage != null;
            var bundleJsString = new String(bundleJs.readAllBytes());
            var htmlString = new String(htmlPage.readAllBytes());
            // var fontwolfString = Base64.encodeBase64(fontwolf.readAllBytes());
            // var fontwolfTwoString = Base64.encodeBase64(fontwolfTwo.readAllBytes());

            htmlString = EmbedAssets(htmlString, "#script#", bundleJsString);
            // htmlString = EmbedAssets(htmlString, "#fontwoff#", new
            // String(fontwolfString));
            // htmlString = EmbedAssets(htmlString, "#fontwoff2#", new
            // String(fontwolfTwoString));
            return htmlString;
        } catch (IOException e) {
            MsiMod.LOGGER.error("Failed to load bundle.js");
            return "Failed to load bundle.js";
        }
    }

    private String EmbedAssets(String htmlString, String assetName, String assetString) {
        return htmlString.replace(assetName, assetString);
    }
}
