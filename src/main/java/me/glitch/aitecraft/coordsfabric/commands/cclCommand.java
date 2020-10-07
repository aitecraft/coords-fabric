package me.glitch.aitecraft.coordsfabric.commands;

import java.util.ArrayList;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import me.glitch.aitecraft.coordsfabric.coord.Coord;
import me.glitch.aitecraft.coordsfabric.util.CommonText;

public final class cclCommand {

    private ArrayList<Coord> coordsList;
    public final static String command = "ccl";

    public cclCommand(ArrayList<Coord> coordsList) {
        this.coordsList = coordsList;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, Boolean dedicated) {
        dispatcher.register(CommandManager.literal(command)
        .then(CommandManager.argument("Search Term", StringArgumentType.greedyString())
            .executes(this::executeSearchTermArg))
        .executes(this::executeNoArgs));
    }

    private int executeNoArgs(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (this.listEmpty()) {
            context.getSource().sendFeedback(CommonText.emptyListError, false);
            return -1;
        }

        context.getSource().sendFeedback(CommonText.separator, false);
        
        for (int i = this.coordsList.size() - 1; i >= 0; i--) {
            context.getSource().sendFeedback(this.coordsList.get(i).toShortText(), false);
        }

        context.getSource().sendFeedback(CommonText.separator, false);

        return 1;
    }

    private int executeSearchTermArg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (this.listEmpty()) {
            context.getSource().sendFeedback(CommonText.emptyListError, false);
            return -1;
        }
        
        String searchTerm = StringArgumentType.getString(context, "Search Term");
        boolean found = false;

        for (int i = this.coordsList.size() - 1; i >= 0; i--) {
            if (this.coordsList.get(i).dataContains(searchTerm)) {
                if (!found) {
                    context.getSource().sendFeedback(CommonText.separator, false);
                    context.getSource().sendFeedback(CommonText.searchStart(searchTerm), false);
                }

                found = true;
                context.getSource().sendFeedback(this.coordsList.get(i).toShortText(), false);
            }
        }

        if (!found) {
            context.getSource().sendFeedback(CommonText.termNotFoundError(command), false);
            return -1;
        }

        context.getSource().sendFeedback(CommonText.separator, false);

        return 1;
    }

    private boolean listEmpty() {
        return this.coordsList.isEmpty();
    }
}