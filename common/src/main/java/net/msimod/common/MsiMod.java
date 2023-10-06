package net.msimod.common;

import com.mojang.authlib.GameProfile;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.msimod.common.chat.ChatEventHandler;
import net.msimod.common.chat.ChatLogger;
import net.msimod.common.networking.server.ApiServer;

import java.util.Objects;
import java.util.UUID;

public class MsiMod {
    /// The id fot the mod
    public static final String MOD_ID = "msimod";

    /// The static reference to the server
    public static MinecraftServer MINECRAFT_SERVER;

    /// The static reference to the server player id
    public static UUID SERVER_PLAYER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /// The static reference to the server player
    public static ServerPlayer SERVER_PLAYER;

    /// Hosts the API server
    private static final ApiServer API_SERVER = new ApiServer();

    /// The chat logger
    public static final ChatLogger CHAT_LOGGER = new ChatLogger();

    public static void init() throws Exception {
        // Stores the server reference as static each tick
        TickEvent.SERVER_POST.register((server) -> MINECRAFT_SERVER = server);
        // Add the server player to the server on the first tick
        LifecycleEvent.SERVER_STARTED.register(MsiMod::addPlayer);

        // Register the chat event handler
        ChatEvent.RECEIVED.register(new ChatEventHandler());

        // Host the server
        API_SERVER.start();
    }

    /// Add the server player to the server
    private static void addPlayer(MinecraftServer server) {
        // Add a player to the server
        if (server.getProfileCache().get(SERVER_PLAYER_UUID).isEmpty()) {
            server.getProfileCache().add(new GameProfile(SERVER_PLAYER_UUID, "Server"));
        }
        var gameProfile = server.getProfileCache().get(SERVER_PLAYER_UUID).get();
        SERVER_PLAYER = new ServerPlayer(server, Objects.requireNonNull(server.getLevel(ServerLevel.OVERWORLD)), gameProfile);
    }
}
