package me.glitch.aitecraft.coordsfabric.coord;

import java.io.Serializable;
import java.util.UUID;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import me.glitch.aitecraft.coordsfabric.util.FabricDiscordHelper;

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
        this.saveName = player.getDisplayName().getString();
        this.dimension = player.getWorld().getRegistryKey().getValue().toString();
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
        commandSource.sendMessage(this.toText());
        
        // Inform targetPlayer
        MessageTargetPlayer(targetPlayer, commandSource);
    }


    
    // Broadcast to chat
    public void broadcastToChat(ServerPlayerEntity commandSource) {
        commandSource.getServer().getPlayerManager().broadcast(this.toText(), false);
        FabricDiscordHelper.sendMessage(this.toText());
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
        MutableText optionText = Text.literal("["+ button +"]");
        optionText.setStyle(
            optionText.getStyle().withHoverEvent(
                new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    Text.of(description)
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
        MutableText optionText = Text.literal("["+ button +"]");
        optionText.setStyle(
            optionText.getStyle().withHoverEvent(
                new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    Text.of(desc)
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

        MutableText options = Text.literal("");
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
        message.append(Text.literal(":").formatted(Formatting.WHITE));
        message.append(getTextX("  ", ""));
        message.append(getTextY("  ", ""));
        message.append(getTextZ("  ", ""));
        message.append(getTextDimension(" [", "]"));
        return message;
    }

    private MutableText getTextSaveName(String pre, String post) {
        return Text.literal(pre + this.saveName + post).formatted(Formatting.WHITE);
    }

    private MutableText getTextSaveName() {
        return getTextSaveName("", "");
    }

    private MutableText getTextX(String pre, String post) {
        return Text.literal(pre + "X = " + this.xValue + post).formatted(Formatting.RED);
    }

    private MutableText getTextX() {
        return getTextX("", "");
    }

    private MutableText getTextY(String pre, String post) {
        return Text.literal(pre + "Y = " + this.yValue + post).formatted(Formatting.GREEN);
    }

    private MutableText getTextY() {
        return getTextY("", "");
    }

    private MutableText getTextZ(String pre, String post) {
        return Text.literal(pre + "Z = " + this.zValue + post).formatted(Formatting.BLUE);
    }

    private MutableText getTextZ() {
        return getTextZ("", "");
    }

    private MutableText getTextDimension(String pre, String post) {
        return Text.literal(pre + this.dimension + post).formatted(Formatting.WHITE);
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
        MutableText messageToTargetPlayer = commandSource.getDisplayName().copy().formatted(Formatting.GRAY);
        messageToTargetPlayer.append(Text.literal(" has seen your coordinates.").formatted(Formatting.GRAY));

        targetPlayer.sendMessage(messageToTargetPlayer);
    }

    public void rename(String newName) {
        this.saveName = newName;
    }

    public String getName() {
        return this.saveName;
    }

}