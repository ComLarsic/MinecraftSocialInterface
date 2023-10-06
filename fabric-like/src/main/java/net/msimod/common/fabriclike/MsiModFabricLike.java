package net.msimod.common.fabriclike;

import net.msimod.common.MsiMod;

public class MsiModFabricLike {
    public static void init() {
        try {
            MsiMod.init();
        } catch (Exception e) {
            e.printStackTrace(); // TODO: Replace with a better logging system
        }
    }
}
