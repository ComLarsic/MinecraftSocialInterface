package net.msimod.common.networking.servlet;

import com.google.gson.Gson;
import net.msimod.common.MsiMod;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/// The servlet that handles the chat log
public class ChatLogServlet extends HttpServlet {
    /// Get the chat log
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var chatLog = MsiMod.CHAT_LOGGER.getChatLog();
        var gson = new Gson();
        response.setContentType("application/json");
        gson.toJson(chatLog, response.getWriter());
    }
}
