package me.glitch.aitecraft.coordsfabric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import me.glitch.aitecraft.coordsfabric.commands.*;
import me.glitch.aitecraft.coordsfabric.coord.Coord;
import me.glitch.aitecraft.coordsfabric.util.SchedulerWrapper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class CommandRegistar implements ModInitializer {

    @Override
    public void onInitialize()
    {
        CommandRegistrationCallback.EVENT.register(ccCommand::register);

        ArrayList<Coord> coordsList = new ArrayList<Coord>();
        HashMap<UUID, ScheduledFuture<?>> tasks = new HashMap<UUID, ScheduledFuture<?>>();
        SchedulerWrapper wrapper = new SchedulerWrapper();

        ccsCommand ccs_Command = new ccsCommand(coordsList);
        ccscCommand ccsc_Command = new ccscCommand(coordsList);
        cclCommand ccl_Command = new cclCommand(coordsList);
        ccGetCommand cc_Get_Command = new ccGetCommand(coordsList);
        ccGetDeleteCommand cc_Get_Delete_Command = new ccGetDeleteCommand(coordsList);
        ccGetRenameCommand cc_Get_Rename_Command = new ccGetRenameCommand(coordsList);
        ccGetOptionsCommand cc_Get_Options_Command = new ccGetOptionsCommand(coordsList);
        ccGetDisplayCommand cc_Get_Display_Command = new ccGetDisplayCommand(coordsList, wrapper, tasks);
        ccDisplayClearCommand cc_Display_Clear_Command = new ccDisplayClearCommand(tasks);

        CommandRegistrationCallback.EVENT.register(ccs_Command::register);
        CommandRegistrationCallback.EVENT.register(ccsc_Command::register);
        CommandRegistrationCallback.EVENT.register(ccl_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Delete_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Rename_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Options_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Display_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Display_Clear_Command::register);

        CoordsFileManager file_manager = new CoordsFileManager(coordsList);
        TaskCleanup taskCleanup = new TaskCleanup(wrapper);

        ServerLifecycleEvents.SERVER_STARTED.register(file_manager);
        ServerLifecycleEvents.SERVER_STOPPING.register(file_manager);
        ServerLifecycleEvents.SERVER_STARTED.register(taskCleanup);
        ServerLifecycleEvents.SERVER_STOPPING.register(taskCleanup);
    }
}