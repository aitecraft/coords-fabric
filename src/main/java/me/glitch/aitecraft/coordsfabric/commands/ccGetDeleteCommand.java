package me.glitch.aitecraft.coordsfabric.commands;

import java.util.ArrayList;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import me.glitch.aitecraft.coordsfabric.coord.Coord;
import me.glitch.aitecraft.coordsfabric.util.CommonText;

public final class ccGetDeleteCommand {
    private ArrayList<Coord> coordsList;

    public ccGetDeleteCommand(ArrayList<Coord> coordsList) {
        this.coordsList = coordsList;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal("cc-get-delete")
        .then(CommandManager.argument("Coord UUID", UuidArgumentType.uuid())
            .executes(this::executeCoordUUIDArg))
        );
    }

    private int executeCoordUUIDArg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        UUID uuidArg = UuidArgumentType.getUuid(context, "Coord UUID");

        boolean found = false;

        for (Coord coord : coordsList) {
            if (coord.getUUID().equals(uuidArg)) {
                context.getSource().sendFeedback(CommonText.coordDeletedMessage(coord), true);
                coordsList.remove(coord);
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