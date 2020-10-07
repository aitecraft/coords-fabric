package me.glitch.aitecraft.coordsfabric.commands;

import java.util.ArrayList;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import me.glitch.aitecraft.coordsfabric.coord.Coord;
import me.glitch.aitecraft.coordsfabric.util.CommonText;

public final class ccGetRenameCommand {
    private ArrayList<Coord> coordsList;
    public final static String command = "cc-get-rename";

    public ccGetRenameCommand(ArrayList<Coord> coordsList) {
        this.coordsList = coordsList;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal(command)
        .then(CommandManager.argument("Coord UUID", UuidArgumentType.uuid())
            .then(CommandManager.argument("New Name", StringArgumentType.greedyString())
            .executes(this::executeCoordUUID_NewNameArgs)))
        );
    }

    private int executeCoordUUID_NewNameArgs(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        UUID uuidArg = UuidArgumentType.getUuid(context, "Coord UUID");
        String new_name = StringArgumentType.getString(context, "New Name");

        boolean found = false;

        for (Coord coord : coordsList) {
            if (coord.getUUID().equals(uuidArg)) {
                context.getSource().sendFeedback(CommonText.coordRenamedMessage(coord, new_name), true);
                coord.rename(new_name);
                found = true;
                break;
            }
        }

        if (!found) {
            context.getSource().sendFeedback(CommonText.coordUUIDNotFoundError, false);
        }

        // Return a result. -1 is failure, 0 is a pass and 1 is success.
        return 1;
    }
}