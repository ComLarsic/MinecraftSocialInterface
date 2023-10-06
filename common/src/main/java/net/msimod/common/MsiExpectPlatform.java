package net.msimod.common;

import dev.architectury.injectables.annotations.ExpectPlatform;
import java.nio.file.Path;

public class MsiExpectPlatform {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        return Path.of("");
    }
}
