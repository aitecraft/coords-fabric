package me.glitch.aitecraft.coordsfabric.util;

import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import me.glitch.aitecraft.coordsfabric.commands.cclCommand;
import me.glitch.aitecraft.coordsfabric.coord.Coord;

public abstract class CommonText {
    public static final Text separator = Text.of(
        "-----------------------"
    );

	public static final MutableText emptyListError = Text.literal(
        "No saved coordinates found. Please save a coordinate using /ccs before using this command."
    ).formatted(Formatting.RED);

    public static final MutableText termNotFoundError(String command) { 
        return Text.literal(
            "No saved coordinates with specified name found. Try using /"+ command +" only."
        ).formatted(Formatting.RED);
    }

    public static final MutableText coordUUIDNotFoundError = Text.literal(
        "The specified coordinate was not found. It was likely deleted. Please run /"+cclCommand.command+" again."
    ).formatted(Formatting.RED);

    public static final MutableText coordDeletedMessage(Coord coord) {
        return Text.literal("Coord deleted: ").append(coord.toText());
    }

	public static final MutableText coordRenamedMessage(Coord coord, String new_name) {
        return Text.literal("Coord renamed from ").append(coord.getName()).append(" to ").append(new_name);
	}

	public static Text searchStart(String searchTerm) {
		return Text.of(
            "Search results for: " + searchTerm
        );
	}
}