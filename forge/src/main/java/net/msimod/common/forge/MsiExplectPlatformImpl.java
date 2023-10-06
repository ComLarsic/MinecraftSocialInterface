package net.msimod.common.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import java.nio.file.Path;

public class MsiExplectPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
