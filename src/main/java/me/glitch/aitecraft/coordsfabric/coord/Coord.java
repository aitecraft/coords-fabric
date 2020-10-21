package me.glitch.aitecraft.coordsfabric.coord;

import java.io.Serializable;
import java.util.UUID;

import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
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

    public Coord(BlockPos position, String dimension, String CoordName) {
        this.xValue = position.getX();
        this.yValue = position.getY();
        this.zValue = position.getZ();
        this.dimension = dimension;
        
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

        // This does show up in Fabric Discord but does not for players that aren't operators.
        commandSource.getCommandSource().sendFeedback(this.toText(), true); 
        */
        commandSource.getServer().getPlayerManager().broadcastChatMessage(this.toText(), MessageType.CHAT, Util.NIL_UUID);
    }

    public MutableText toListText() {
        MutableText message = getTextSaveName().formatted(Formatting.YELLOW);

        MutableText hover_text = getTextX("", "\n");
        hover_text.append(getTextY("", "\n"));
        hover_text.append(getTextZ("", "\n"));
        hover_text.append(getTextDimension());

        message.setStyle(
            message.getStyle().withHoverEvent(
                new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    hover_text
                )
            ).withClickEvent(
                new ClickEvent(
                    Action.RUN_COMMAND,
                    "/cc-get-options " + this.uuid
                )
            )
        );

        return message;
    }

    public MutableText toDisplayText() {
        MutableText message = getTextX("", " ").formatted(Formatting.WHITE);
        message.append(getTextY("", " ").formatted(Formatting.WHITE));
        message.append(getTextZ().formatted(Formatting.WHITE));
        return message;
    }

    private MutableText createOptionText(String button, String description, String command, boolean suggest) {
        MutableText optionText = new LiteralText("["+ button +"]");
        optionText.setStyle(
            optionText.getStyle().withHoverEvent(
                new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new LiteralText(description)
                )
            ).withClickEvent(
                new ClickEvent(
                    suggest ? ClickEvent.Action.SUGGEST_COMMAND : ClickEvent.Action.RUN_COMMAND,
                    command + " " + this.uuid + (suggest ? " " : "")
                )
            )
        );
        optionText.formatted(Formatting.YELLOW);
        return optionText;
    }

    private MutableText createCopyCoordOptionText(String button, String desc) {
        MutableText optionText = new LiteralText("["+ button +"]");
        optionText.setStyle(
            optionText.getStyle().withHoverEvent(
                new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new LiteralText(desc)
                )
            ).withClickEvent(
                new ClickEvent(
                    ClickEvent.Action.COPY_TO_CLIPBOARD,
                    "/execute in " 
                        + this.dimension + " run tp @s " 
                        + this.xValue + " "
                        + this.yValue + " "
                        + this.zValue
                )
            )
        );
        optionText.formatted(Formatting.YELLOW);
        return optionText;
    }

    public MutableText toTextWithOptions() {
        MutableText message = this.toText();
        message.append("\n");

        MutableText options = new LiteralText("");
        options.append(createOptionText("GET", "Show in public chat", "/cc-get", false));
        options.append(" ");
        options.append(createOptionText("DEL", "Delete", "/cc-get-delete", false));
        options.append(" ");
        options.append(createOptionText("REN", "Rename", "/cc-get-rename", true));
        options.append(" ");
        options.append(createOptionText("DIS", "Display above hotbar", "/cc-get-display", false));
        options.append(" ");
        options.append(createCopyCoordOptionText("CPY", "Copy to clipboard"));
        message.append(options);

        return message;
    }

    public MutableText toText() {
        MutableText message = getTextSaveName();
        message.append(new LiteralText(":").formatted(Formatting.WHITE));
        message.append(getTextX("  ", ""));
        message.append(getTextY("  ", ""));
        message.append(getTextZ("  ", ""));
        message.append(getTextDimension(" [", "]"));
        return message;
    }

    private MutableText getTextSaveName(String pre, String post) {
        return new LiteralText(pre + this.saveName + post).formatted(Formatting.WHITE);
    }

    private MutableText getTextSaveName() {
        return getTextSaveName("", "");
    }

    private MutableText getTextX(String pre, String post) {
        return new LiteralText(pre + "X = " + this.xValue + post).formatted(Formatting.RED);
    }

    private MutableText getTextX() {
        return getTextX("", "");
    }

    private MutableText getTextY(String pre, String post) {
        return new LiteralText(pre + "Y = " + this.yValue + post).formatted(Formatting.GREEN);
    }

    private MutableText getTextY() {
        return getTextY("", "");
    }

    private MutableText getTextZ(String pre, String post) {
        return new LiteralText(pre + "Z = " + this.zValue + post).formatted(Formatting.BLUE);
    }

    private MutableText getTextZ() {
        return getTextZ("", "");
    }

    private MutableText getTextDimension(String pre, String post) {
        return new LiteralText(pre + this.dimension + post).formatted(Formatting.WHITE);
    }

    private MutableText getTextDimension() {
        return getTextDimension("", "");
    }

    public boolean dataContains(String term) {
        boolean match = false;

        for(String t : term.split(" ")) {
            match = true;
            for (String s : t.split("\\+")) {
                match = (match && (hasTerm(s)));
            }
        }

        return match;
    }

    private boolean hasTerm(String term) {
        return 
            this.saveName.toLowerCase().contains(term.toLowerCase()) ||
            this.dimension.toLowerCase().contains(term.toLowerCase())        
        ;
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

    public void rename(String newName) {
        this.saveName = newName;
    }

    public String getName() {
        return this.saveName;
    }

}