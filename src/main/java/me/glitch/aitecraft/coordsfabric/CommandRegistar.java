package me.glitch.aitecraft.coordsfabric;

import java.util.ArrayList;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import me.glitch.aitecraft.coordsfabric.commands.ccCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccGetCommand;
import me.glitch.aitecraft.coordsfabric.commands.ccGetDeleteCommand;
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

        ccsCommand ccs_Command = new ccsCommand(coordsList);
        cclCommand ccl_Command = new cclCommand(coordsList);
        ccGetCommand cc_Get_Command = new ccGetCommand(coordsList);
        ccGetDeleteCommand cc_Get_Delete_Command = new ccGetDeleteCommand(coordsList);
        ccGetRenameCommand cc_Get_Rename_Command = new ccGetRenameCommand(coordsList);
        ccGetOptionsCommand cc_Get_Options_Command = new ccGetOptionsCommand(coordsList);

        CommandRegistrationCallback.EVENT.register(ccs_Command::register);
        CommandRegistrationCallback.EVENT.register(ccl_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Delete_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Rename_Command::register);
        CommandRegistrationCallback.EVENT.register(cc_Get_Options_Command::register);

        CoordsFileManager file_manager = new CoordsFileManager(coordsList);

        ServerLifecycleEvents.SERVER_STARTED.register(file_manager);
        ServerLifecycleEvents.SERVER_STOPPING.register(file_manager);
    }
}