package me.glitch.aitecraft.coordsfabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import me.glitch.aitecraft.coordsfabric.coord.Coord;

public final class ccCommand {

    public final static String command = "cc";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal(command)
        .then(CommandManager.argument("Player Name", EntityArgumentType.player())
            .executes(ccCommand::executePlayerArg))
        .executes(ccCommand::executeNoArgs));
    }

    private static int executeNoArgs(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity commandSource = context.getSource().getPlayer();

        Coord playerCoord = new Coord(commandSource); 
        playerCoord.broadcastToChat(commandSource);

        return 1;
    }

    private static int executePlayerArg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity commandSource = context.getSource().getPlayer();
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "Player Name");

        Coord targetPlayerCoord = new Coord(targetPlayer);
        targetPlayerCoord.sendToSpecificPlayer(commandSource, targetPlayer);
        
        // Return a result. -1 is failure, 0 is a pass and 1 is success.
        return 1;
    }
}