package me.glitch.aitecraft.coordsfabric.util;

import net.minecraft.text.MutableText;
import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;

public final class FabricDiscordManager {
    public static void sendMessage(MutableText message) {
        final class CompatText implements Message {
            private String message;
    
            public CompatText(MutableText text) {
                message = text.getString();
            }
            
            @Override
            public String getMessage() {
                return message;
            }
    
            @Override
            public MessageObjectType getType() {
                return MessageObjectType.STRING;
            }
    
            @Override
            public TextType getTextType() {
                return TextType.LITERAL;
            }
            
        }
    
        CompatText msg = new CompatText(message);
        FDLink.getMessageSender().sendMessage(msg);
    }
}