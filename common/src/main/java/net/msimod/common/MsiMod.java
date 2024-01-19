package net.msimod.common;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.ServerOpListEntry;
import net.msimod.common.chat.ChatLogEventRegistry;
import net.msimod.common.chat.ChatLogger;
import net.msimod.common.networking.server.ApiServer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.UUID;

public class MsiMod {
    /// The id fot the mod
    public static final String MOD_ID = "msimod";
    // The logger for the mod
    public static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(MOD_ID);

    /// The mod config
    public static MsiModConfig CONFIG;

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

    /// Dirty way to check if the server has started
    private static boolean isServerStarted = false;

    public static void init() throws Exception {
        // Load the config
        CONFIG = loadConfig();

        // Stores the server reference as static each tick
        TickEvent.SERVER_POST.register((server) -> MINECRAFT_SERVER = server);
        // Add the server player to the server on the first tick
        LifecycleEvent.SERVER_STARTED.register(MsiMod::addPlayer);

        // Register the chat event handlers
        ChatLogEventRegistry.RegisterEventHandlers(CHAT_LOGGER);

        // Host the server
        LifecycleEvent.SERVER_LEVEL_LOAD.register(listener -> {
            if (isServerStarted)
                return;
            try {
                API_SERVER.start();
                isServerStarted = true;
            } catch (Exception e) {
                LOGGER.error("Failed to start API server: {}", e);
            }
        });
    }

    /// Load the config
    private static MsiModConfig loadConfig() {
        try {
            var fileStream = new FileReader("config/msimod.json");
            var config = new Gson().fromJson(fileStream, MsiModConfig.class);
            return config;
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not find config file, using default config");
            var config = new MsiModConfig();
            // Save the default config to file
            var json = new Gson().toJson(config);
            try {
                var fileStream = new java.io.FileWriter("config/msimod.json");
                fileStream.write(json);
                fileStream.close();
            } catch (Exception ex) {
                LOGGER.error("Could not save default config file");
            }
            return config;
        }
    }

    /// Add the server player to the server
    private static void addPlayer(MinecraftServer server) {
        // Add a player to the server
        if (server.getProfileCache().get(SERVER_PLAYER_UUID).isEmpty()) {
            var profile = new GameProfile(SERVER_PLAYER_UUID, ">");
            server.getProfileCache().add(profile);
        }
        var gameProfile = server.getProfileCache().get(SERVER_PLAYER_UUID).get();
        SERVER_PLAYER = new ServerPlayer(server, Objects.requireNonNull(server.getLevel(ServerLevel.OVERWORLD)),
                gameProfile);
    }
}
