package me.glitch.aitecraft.coordsfabric.commands;

import java.util.ArrayList;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import me.glitch.aitecraft.coordsfabric.coord.Coord;

public final class ccscCommand {

    private ArrayList<Coord> coordsList;
    public static final String command = "ccsc";

    public ccscCommand(ArrayList<Coord> coordsList) {
        this.coordsList = coordsList;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal(command)
        .then(CommandManager.argument("Position", BlockPosArgumentType.blockPos())
            .then(CommandManager.argument("Dimension", DimensionArgumentType.dimension())
            .then(CommandManager.argument("Save Name", StringArgumentType.greedyString())
            .executes(this::execute))))
        );
    }

    private int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity commandSource = context.getSource().getPlayerOrThrow();
        
        BlockPos position = BlockPosArgumentType.getBlockPos(context, "Position");
        String dimension = DimensionArgumentType.getDimensionArgument(context, "Dimension").getRegistryKey().getValue().toString();
        String saveName = StringArgumentType.getString(context, "Save Name");

        Coord playerCoord = new Coord(position, dimension, saveName);
        this.coordsList.add(playerCoord);
        playerCoord.broadcastToChat(commandSource);
        
        // Return a result. -1 is failure, 0 is a pass and 1 is success.
        return 1;
    }
}