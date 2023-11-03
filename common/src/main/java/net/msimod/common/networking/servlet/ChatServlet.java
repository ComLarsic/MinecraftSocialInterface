package net.msimod.common.networking.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.msimod.common.MsiMod;

/// The servlet that opens routes for sending chat messages via the api
public class ChatServlet extends HttpServlet {
    /// Post a message to the chat
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        var server = MsiMod.MINECRAFT_SERVER;
        var message = request.getParameter("message");

        // Check if message is command
        if (message.startsWith("/")) {
            processCommand(server, message);
            return;
        }
        // Broadcast the message to the server if it is not a command
        broadcastChatMessage(server, message);
    }

    /**
     * Process a command
     * 
     * @param server  The server to process the command on
     * @param message The message with the command to execute
     */
    private void processCommand(MinecraftServer server, String message) {
        var commandManager = server.getCommands();
        var parsedCommand = commandManager.getDispatcher().parse(message.substring(1),
                MsiMod.SERVER_PLAYER.createCommandSourceStack());
        try {
            commandManager.getDispatcher().execute(parsedCommand);
        } catch (CommandSyntaxException e) {
            MsiMod.CHAT_LOGGER.append(null, ">", e.getMessage());
        }
        return;
    }

    /**
     * Broadcast a chat message to the server
     * 
     * @param server  The server to broadcast the message to
     * @param message The message to broadcast
     */
    private void broadcastChatMessage(MinecraftServer server, String message) {
        server.getPlayerList().broadcastChatMessage(
                PlayerChatMessage.system(message),
                MsiMod.SERVER_PLAYER,
                ChatType.bind(ChatType.CHAT, MsiMod.SERVER_PLAYER.createCommandSourceStack()));
    }
}
