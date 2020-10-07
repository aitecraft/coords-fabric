package me.glitch.aitecraft.coordsfabric.util;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import me.glitch.aitecraft.coordsfabric.commands.cclCommand;
import me.glitch.aitecraft.coordsfabric.coord.Coord;

public abstract class CommonText {
    public static final MutableText separator = (new LiteralText(
        "-----------------------"
    ));

	public static final MutableText emptyListError = (new LiteralText(
        "No saved coordinates found. Please save a coordinate using /ccs before using this command."
    )).formatted(Formatting.RED);

    public static final MutableText termNotFoundError(String command) { 
        return new LiteralText(
        "No saved coordinates with specified name found. Try using /"+ command +" only."
        ).formatted(Formatting.RED);
    }

    public static final MutableText coordUUIDNotFoundError = (new LiteralText(
        "The specified coordinate was not found. It was likely deleted. Please run /"+cclCommand.command+" again."
    )).formatted(Formatting.RED);

    public static final MutableText coordDeletedMessage(Coord coord) {
        return new LiteralText("Coord deleted: ").append(coord.toText());
    }

	public static final MutableText coordRenamedMessage(Coord coord, String new_name) {
        return new LiteralText("Coord renamed from ").append(coord.getName()).append(" to ").append(new_name);
	}

	public static LiteralText searchStart(String searchTerm) {
		return new LiteralText(
            "Search results for: " + searchTerm
        );
	}
}