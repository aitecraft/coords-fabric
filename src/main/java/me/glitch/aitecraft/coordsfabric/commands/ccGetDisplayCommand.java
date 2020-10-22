package me.glitch.aitecraft.coordsfabric.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import me.glitch.aitecraft.coordsfabric.coord.Coord;
import me.glitch.aitecraft.coordsfabric.util.CommonText;
import me.glitch.aitecraft.coordsfabric.util.SchedulerWrapper;

public final class ccGetDisplayCommand {
    private ArrayList<Coord> coordsList;
    private SchedulerWrapper wrapper;
    private HashMap<UUID, ScheduledFuture<?>> tasks;
    public final static String command = "cc-get-display";

    public ccGetDisplayCommand(ArrayList<Coord> coordsList, SchedulerWrapper wrapper, HashMap<UUID, ScheduledFuture<?>> tasks) {
        this.coordsList = coordsList;
        this.wrapper = wrapper;
        this.tasks = tasks;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal(command)
        .then(CommandManager.argument("Coord UUID", UuidArgumentType.uuid())
            .executes(this::executeCoordUUIDArg))
        );
    }

    private int executeCoordUUIDArg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity commandSource = context.getSource().getPlayer();
        UUID uuidArg = UuidArgumentType.getUuid(context, "Coord UUID");

        boolean found = false;
        
        for (Coord coord : coordsList) {
            if (coord.getUUID().equals(uuidArg)) {
                display(coord, commandSource);
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

    private void display(Coord coord, ServerPlayerEntity player) {
        MutableText message = coord.toDisplayText();
        
        if (tasks.containsKey(player.getUuid())) {
            tasks.get(player.getUuid()).cancel(false);
        }
        
        Runnable task = new Runnable(){
            @Override
            public void run() {
                player.sendMessage(message, true);
            }
        };

        ScheduledFuture<?> taskFuture = wrapper.scheduler.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
        tasks.put(player.getUuid(), taskFuture);
    }
}