package net.msimod.common.fabric;

import org.quiltmc.loader.api.QuiltLoader;
import java.nio.file.Path;

public class MsiExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return QuiltLoader.getConfigDir();
    }
}
