package net.msimod.common.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class MsiExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
