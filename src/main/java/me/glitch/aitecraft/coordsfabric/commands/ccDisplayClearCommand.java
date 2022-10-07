package me.glitch.aitecraft.coordsfabric.commands;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public final class ccDisplayClearCommand {
    private HashMap<UUID, ScheduledFuture<?>> tasks;
    public final static String command = "cc-display-clear";

    public ccDisplayClearCommand(HashMap<UUID, ScheduledFuture<?>> tasks) {
        this.tasks = tasks;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal(command)
            .executes(this::executeNoArgs)
        );
    }

    private int executeNoArgs(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        UUID playerUuid = context.getSource().getPlayerOrThrow().getUuid();
        
        if (tasks.containsKey(playerUuid)) {
            tasks.get(playerUuid).cancel(false);
        }

        // Return a result. -1 is failure, 0 is a pass and 1 is success.
        return 1;
    }
}