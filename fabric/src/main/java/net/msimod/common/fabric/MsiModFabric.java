package net.msimod.common.fabric;

import net.fabricmc.api.ModInitializer;
import net.msimod.common.fabriclike.MsiModFabricLike;

public class MsiModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MsiModFabricLike.init();
    }
}
