package me.glitch.aitecraft.coordsfabric.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

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

public final class ccGetDisplayCommand {
    private ArrayList<Coord> coordsList;
    private Timer timer;
    private HashMap<UUID, TimerTask> tasks;
    public final static String command = "cc-get-display";

    public ccGetDisplayCommand(ArrayList<Coord> coordsList, Timer timer, HashMap<UUID, TimerTask> tasks) {
        this.coordsList = coordsList;
        this.timer = timer;
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
            tasks.get(player.getUuid()).cancel();
        }
        
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                player.sendMessage(message, true);
            }
        };

        tasks.put(player.getUuid(), task);
        timer.scheduleAtFixedRate(task, 0, 2000);
    }
}