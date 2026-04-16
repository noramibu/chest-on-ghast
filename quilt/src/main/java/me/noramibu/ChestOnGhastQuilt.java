package me.noramibu;

import net.fabricmc.api.ModInitializer;

public final class ChestOnGhastQuilt implements ModInitializer {
    @Override
    public void onInitialize() {
        ChestOnGhastCommon.init();
    }
}
