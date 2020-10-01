package me.glitch.aitecraft.coordsfabric.coord;

import java.io.Serializable;
import java.util.UUID;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

// This is the custom data type used for every single set of coordinates.
public class Coord implements Serializable
{
    private static final long serialVersionUID = 9174817348858452097L;
    
    private int xValue;
    private int yValue;
    private int zValue;
    private String saveName;
    private UUID uuid; 
    private String dimension;

    public Coord(ServerPlayerEntity player) {
        BlockPos player_pos = player.getBlockPos();
        this.xValue = player_pos.getX();
        this.yValue = player_pos.getY();
        this.zValue = player_pos.getZ();
        this.saveName = player.getDisplayName().asString();
        this.dimension = player.getServerWorld().getRegistryKey().getValue().toString();
    }

    public Coord(ServerPlayerEntity player, String CoordName) {
        this(player);
        this.saveName = CoordName;
        this.uuid = UUID.randomUUID();
    }

    public void sendToSpecificPlayer(ServerPlayerEntity commandSource, ServerPlayerEntity targetPlayer) {
        commandSource.sendSystemMessage(this.toText(), targetPlayer.getUuid());
        
        // Inform targetPlayer
        MessageTargetPlayer(targetPlayer, commandSource);
    }


    
    // Broadcast to chat
    public void broadcastToChat(ServerPlayerEntity commandSource) {
        
        /*
        // This never gets shown in client chat.
        commandSource.getServer().sendSystemMessage(
            this.toText(), commandSource.getUuid()
        );

        // This gets shown in both server and client chat, but not in Fabric-discord
        commandSource.getServer().getPlayerManager().broadcastChatMessage(this.toText(), MessageType.CHAT ,commandSource.getUuid());

        // This only gets shown in client chat
        commandSource.getServer().getPlayerManager().sendToAll(new GameMessageS2CPacket(this.toText(), MessageType.CHAT, commandSource.getUuid()));

        // Same behaviour as above. May want to use this for better perf?
        commandSource.getServer().sendSystemMessage(
            this.toText(), Util.NIL_UUID
        );
        commandSource.getServer().getPlayerManager().broadcastChatMessage(this.toText(), MessageType.CHAT ,Util.NIL_UUID);
        commandSource.getServer().getPlayerManager().sendToAll(new GameMessageS2CPacket(this.toText(), MessageType.CHAT, Util.NIL_UUID));

        */
        commandSource.getCommandSource().sendFeedback(this.toText(), true);
    }
    
    public enum TextAction {
        NONE,
        GET,
        DELETE
    }

    public MutableText toText(TextAction action) {
        MutableText message = (new LiteralText(this.saveName)).formatted(Formatting.WHITE);
        message.append(new LiteralText(":").formatted(Formatting.WHITE));
        message.append(new LiteralText("  X = " + this.xValue).formatted(Formatting.RED));
        message.append(new LiteralText("  Y = " + this.yValue).formatted(Formatting.GREEN));
        message.append(new LiteralText("  Z = " + this.zValue).formatted(Formatting.BLUE));
        message.append(new LiteralText(" [" + this.dimension + "]").formatted(Formatting.WHITE));

        if (action == TextAction.GET)
        {
            message.setStyle(
                message.getStyle().withHoverEvent(
                    new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new LiteralText("Show in public chat")
                    )
                ).withClickEvent(
                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cc-get " + this.uuid)
                )
            );
        } 
        else if (action == TextAction.DELETE)
        {
            message.setStyle(
                message.getStyle().withHoverEvent(
                    new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new LiteralText("Delete").formatted(Formatting.RED)
                    )
                ).withClickEvent(
                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cc-get-delete " + this.uuid)
                )
            );
        }

        return message;
    }

    public MutableText toText() {
        return toText(TextAction.NONE);
    }

    public boolean saveNameContains(String term) {
        return this.saveName.toLowerCase().contains(term.toLowerCase());
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getUUIDString() {
        return this.uuid.toString();
    }

    // notify specific playerentity (static method)
    private static void MessageTargetPlayer(ServerPlayerEntity targetPlayer, ServerPlayerEntity commandSource) {
        MutableText messageToTargetPlayer = (new LiteralText(commandSource.getDisplayName().asString())).formatted(Formatting.GRAY);
        messageToTargetPlayer.append(new LiteralText(" has seen your coordinates.").formatted(Formatting.GRAY));

        targetPlayer.sendSystemMessage(messageToTargetPlayer, commandSource.getUuid());
    }

}