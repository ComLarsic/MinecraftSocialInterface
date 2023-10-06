package net.msimod.common.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.msimod.common.MsiMod;

@Mod(MsiMod.MOD_ID)
public class MsiModForge {
    public MsiModForge() throws Exception {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(MsiMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MsiMod.init();
    }
}
