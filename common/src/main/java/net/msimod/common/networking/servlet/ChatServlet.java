package net.msimod.common.networking.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.ChatTypeDecoration;
import net.minecraft.network.chat.PlayerChatMessage;
import net.msimod.common.MsiMod;

/// The servlet that opens routes for sending chat messages via the api
public class ChatServlet extends HttpServlet {
    /// Post a message to the chat
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        var server = MsiMod.MINECRAFT_SERVER;
        var message = request.getParameter("message");

        server.getPlayerList().broadcastChatMessage(
                PlayerChatMessage.system(message),
                MsiMod.SERVER_PLAYER,
                ChatType.bind(ChatType.CHAT, MsiMod.SERVER_PLAYER.createCommandSourceStack())
        );
    }
}
