package me.glitch.aitecraft.coordsfabric.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.MutableText;

public final class FabricDiscordHelper implements DedicatedServerModInitializer {
    private static boolean isServer = false;
    private static Method sendMessage;
    private static Object msg_sender;

    private static enum FDLinkStatus {
        NOT_CHECKED,
        AVAILABLE,
        NOT_AVAILABLE,
        REFLECTED
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

        try {
            
            if (status != FDLinkStatus.REFLECTED) {
                // Reflect class
                Class<?> fdlink = Class.forName("fr.arthurbambou.fdlink.FDLink");
                
                // Get method "getMessageSender" in the FDLink class, no arguments
                Method get_sender = fdlink.getDeclaredMethod("getMessageSender");
                
                // Invoke the getMessageSender function
                msg_sender = get_sender.invoke(null);  // null because getMessageSender is a static method.
                
                // Get method "sendToChatChannels" in the class of object returned by getMessageSender with 1 argument, a String
                sendMessage = msg_sender.getClass().getDeclaredMethod("sendToChatChannels", String.class);

                // Set status accordingly so we don't reflect for every outgoing message.
                status = FDLinkStatus.REFLECTED;
            }
            
            // Invoke the sendToChatChannels function through the msg_sender object.
            sendMessage.invoke(msg_sender, message.getString());

        } catch (ClassNotFoundException|NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
            System.out.println("[Coords] [ERROR] You are using an unsupported Fabric-Discord Link version. Please upgrade/downgrade the Fabric-Discord Link version and restart the server. Coords will now stop sending messages via Fabric-Discord Link until you restart the server.");
            status = FDLinkStatus.NOT_AVAILABLE;
        }

    }    
}