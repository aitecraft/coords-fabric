package me.glitch.aitecraft.coordsfabric.util;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.MutableText;

public final class FabricDiscordHelper implements DedicatedServerModInitializer {
    private static boolean isServer = false;

    private static enum FDLinkStatus {
        NOT_CHECKED,
        AVAILABLE,
        NOT_AVAILABLE
    }

    private static FDLinkStatus status = FDLinkStatus.NOT_CHECKED;

    @Override
    public void onInitializeServer() {
        isServer = true;
    }

    public static void sendMessage(MutableText message) {
        if (!isServer) {
            return;
        }

        if (status == FDLinkStatus.NOT_CHECKED) {
            status = 
                FabricLoader.getInstance().isModLoaded("fdlink_common") ? 
                    FDLinkStatus.AVAILABLE : FDLinkStatus.NOT_AVAILABLE;
        }

        if (status == FDLinkStatus.NOT_AVAILABLE) {
            return;
        }

        FabricDiscordManager.sendMessage(message);
    }    
}