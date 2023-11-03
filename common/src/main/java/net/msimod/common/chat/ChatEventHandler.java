package net.msimod.common.chat;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.msimod.common.MsiMod;
import org.jetbrains.annotations.Nullable;

/**
 * The event handler for a chat event
 */
public class ChatEventHandler implements ChatEvent.Received {
    private static ChatLogger CHAT_LOGGER;

    public ChatEventHandler(ChatLogger chatLogger) {
        CHAT_LOGGER = chatLogger;
    }

    @Override
    public EventResult received(@Nullable ServerPlayer player, Component component) {
        if (player == null) {
            MsiMod.LOGGER.info("Non-player Message: " + component.getString());
            return EventResult.pass();
        }
        CHAT_LOGGER.append(player.getUUID(), player.getName().getString(), component.getString());
        return EventResult.pass();
    }
}
