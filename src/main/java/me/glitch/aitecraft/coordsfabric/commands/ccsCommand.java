package me.glitch.aitecraft.coordsfabric.commands;

import java.util.ArrayList;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import me.glitch.aitecraft.coordsfabric.coord.Coord;

public final class ccsCommand {

    private ArrayList<Coord> coordsList;
    public static final String command = "ccs";

    public ccsCommand(ArrayList<Coord> coordsList) {
        this.coordsList = coordsList;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal(command)
        .then(CommandManager.argument("Save Name", StringArgumentType.greedyString())
            .executes(this::executeSaveNameArg))
        );
    }

    private int executeSaveNameArg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity commandSource = context.getSource().getPlayer();
        String saveName = StringArgumentType.getString(context, "Save Name");

        Coord playerCoord = new Coord(commandSource, saveName);
        this.coordsList.add(playerCoord);
        playerCoord.broadcastToChat(commandSource);
        
        // Return a result. -1 is failure, 0 is a pass and 1 is success.
        return 1;
    }
}