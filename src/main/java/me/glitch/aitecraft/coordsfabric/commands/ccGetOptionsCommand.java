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

public final class ccGetOptionsCommand {
    private ArrayList<Coord> coordsList;
    public final static String command = "cc-get-options";

    public ccGetOptionsCommand(ArrayList<Coord> coordsList) {
        this.coordsList = coordsList;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal(command)
        .then(CommandManager.argument("Coord UUID", UuidArgumentType.uuid())
            .executes(this::executeCoordUUIDArg))
        );
    }

    private int executeCoordUUIDArg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        UUID uuidArg = UuidArgumentType.getUuid(context, "Coord UUID");

        boolean found = false;
        
        for (Coord coord : coordsList) {
            if (coord.getUUID().equals(uuidArg)) {
                context.getSource().sendFeedback(coord.toTextWithOptions(), false);
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