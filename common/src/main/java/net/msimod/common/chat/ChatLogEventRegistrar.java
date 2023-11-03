package net.msimod.common.chat;

import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.PlayerEvent;

/**
 * Handles registering the chat events
 */
public class ChatLogEventRegistrar {
    /**
     * Registers the chat events handlers
     */
    public static void RegisterEventHandlers(ChatLogger chatLogger) {
        // Register the chat event handler
        ChatEvent.RECEIVED.register(new ChatEventHandler(chatLogger));
        // Register the event handler for system chat messages
        PlayerEvent.PLAYER_JOIN.register((listener) -> {
            chatLogger.appendNonPlayer(listener.getName().getString() + " joined the server");
        });
        PlayerEvent.PLAYER_QUIT.register((listener) -> {
            chatLogger.appendNonPlayer(listener.getName().getString() + " left the server");
        });
        PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) -> {
            chatLogger.appendNonPlayer(player.getName().getString() + " gained advancement: "
                    + advancement.getChatComponent().getString());
        });
    }
}
