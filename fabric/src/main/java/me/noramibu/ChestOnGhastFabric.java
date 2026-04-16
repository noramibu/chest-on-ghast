package me.noramibu;

import net.fabricmc.api.ModInitializer;

public final class ChestOnGhastFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ChestOnGhastCommon.init();
    }
}
