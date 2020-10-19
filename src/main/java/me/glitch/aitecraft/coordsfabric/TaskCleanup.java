package me.glitch.aitecraft.coordsfabric;

import java.util.HashMap;
import java.util.TimerTask;
import java.util.UUID;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping;
import net.minecraft.server.MinecraftServer;

public final class TaskCleanup implements ServerStopping {
    @Override
    public void onServerStopping(MinecraftServer server) {
        for(TimerTask task : tasks.values()) {
            task.cancel();
        }
    }

    HashMap<UUID, TimerTask> tasks;

    public TaskCleanup(HashMap<UUID, TimerTask> tasks) {
        this.tasks = tasks;
    }
}