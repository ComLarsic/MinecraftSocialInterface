package net.msimod.common.networking.socket.chat;

import java.io.Serializable;
import java.util.LinkedList;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.msimod.common.MsiMod;
import net.msimod.common.auth.XLiveAuth;
import net.msimod.common.chat.ChatLogger.ChatMessageLog;
import net.msimod.common.networking.socket.Message;
import net.msimod.common.networking.socket.MessageHandler;

/**
 * The socket for the chat functionality
 */
public class ChatMessageHandler implements MessageHandler {
    /// The clients connected to the server
    private static LinkedList<Session> _linkedClients = new LinkedList<Session>();

    /**
     * Send a chat message to the clients
     * 
     * @param message
     */
    public static void sendChatToClient(ChatMessageLog message) {
        var gson = new Gson();
        var json = gson.toJson(new Message(Message.Handlers.CHAT, Message.Type.UPDATE, message));
        _linkedClients.forEach(session -> {
            try {
                // Remove the client if not open
                if (!session.isOpen())
                    return;
                session.getRemote().sendString(json);
            } catch (Exception e) {
                MsiMod.LOGGER.info("Disconnected client: {}", e);
                _linkedClients.remove(session);
            }
        });
    }

    @Override
    public void register(Session session, Message message) {
        _linkedClients.add(session);
    }

    @Override
    public void unregister(Session session, Message message) {
        _linkedClients.remove(session);
    }

    @Override
    public void update(Session session, Message message) {
        var user = (String) XLiveAuth.GetXui(message.accessToken).get("gtg");
        var messageStr = (String) message.data;
        var server = MsiMod.MINECRAFT_SERVER;

        // Check if message is command
        if (messageStr.startsWith("/")) {
            processCommand(server, messageStr);
            return;
        }
        // Broadcast the message to the server if it is not a command
        broadcastChatMessage(server, user, messageStr);
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
        } catch (Exception e) {
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
    private void broadcastChatMessage(MinecraftServer server, String player, String message) {
        var messageStr = "[" + player + "]: " + message;
        server.getPlayerList().broadcastChatMessage(
                PlayerChatMessage.system(messageStr),
                MsiMod.SERVER_PLAYER,
                ChatType.bind(ChatType.CHAT, MsiMod.SERVER_PLAYER.createCommandSourceStack()));
        MsiMod.SERVER_PLAYER.setCustomName(Component.literal(""));
    }
}
