package me.glitch.aitecraft.coordsfabric.util;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import me.glitch.aitecraft.coordsfabric.coord.Coord;

public abstract class CommonText {
    public static MutableText emptyListError = (new LiteralText(
        "No saved coordinates found. Please save a coordinate using /ccs before using this command."
    )).formatted(Formatting.RED);

    public static MutableText termNotFoundError = (new LiteralText(
        "No saved coordinates with specified name found. Try using /ccl only or /ccd only."
    )).formatted(Formatting.RED);

    public static MutableText coordUUIDNotFoundError = (new LiteralText(
        "The specified coordinate was not found. It was likely deleted. Please run /ccl or /ccd again."
    )).formatted(Formatting.RED);

    public static MutableText coordDeletedMessage(Coord coord) {
        return new LiteralText("Coord deleted: ").append(coord.toText());
    }
}