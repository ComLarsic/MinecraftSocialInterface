package net.msimod.common.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import net.msimod.common.fabriclike.MsiModFabricLike;

public class MsiModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        MsiModFabricLike.init();
    }
}
