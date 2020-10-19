package me.glitch.aitecraft.coordsfabric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import me.glitch.aitecraft.coordsfabric.commands.ccCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccDisplayClearCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccGetCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccGetDeleteCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccGetDisplayCommand;
import me.glitch.aitecraft.coordsfabric.commands.cclCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccsCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccGetRenameCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccGetOptionsCommand;
import me.glitch.aitecraft.coordsfabric.coord.Coord;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class CommandRegistar implements ModInitializer {

    @Override
    public void onInitialize()
    {
        CommandRegistrationCallback.EVENT.register(ccCommand::register);

        ArrayList<Coord> coordsList = new ArrayList<Coord>();
        Timer timer = new Timer();
        HashMap<UUID, TimerTask> tasks = new HashMap<UUID, TimerTask>();

        ccsCommand ccs_Command = new ccsCommand(coordsList);
        cclCommand ccl_Command = new cclCommand(coordsList);
        ccGetCommand cc_Get_Command = new ccGetCommand(coordsList);
        ccGetDeleteCommand cc_Get_Delete_Command = new ccGetDeleteCommand(coordsList);
        ccGetRenameCommand cc_Get_Rename_Command = new ccGetRenameCommand(coordsList);
        ccGetOptionsCommand cc_Get_Options_Command = new ccGetOptionsCommand(coordsList);
        ccGetDisplayCommand cc_Get_Display_Command = new ccGetDisplayCommand(coordsList, timer, tasks);
        ccDisplayClearCommand cc_Display_Clear_Command = new ccDisplayClearCommand(tasks);

        CommandRegistrationCallback.EVENT.register(ccs_Command::register);
        CommandRegistrationCallback.EVENT.register(ccl_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Delete_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Rename_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Options_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Display_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Display_Clear_Command::register);

        CoordsFileManager file_manager = new CoordsFileManager(coordsList);
        TaskCleanup taskCleanup = new TaskCleanup(tasks);

        ServerLifecycleEvents.SERVER_STARTED.register(file_manager);
        ServerLifecycleEvents.SERVER_STOPPING.register(file_manager);
        ServerLifecycleEvents.SERVER_STOPPING.register(taskCleanup);
    }
}