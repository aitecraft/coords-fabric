package me.glitch.aitecraft.coordsfabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping;
import net.minecraft.server.MinecraftServer;

import me.glitch.aitecraft.coordsfabric.util.SchedulerWrapper;

public final class TaskCleanup implements ServerStopping, ServerStarted {
    @Override
    public void onServerStopping(MinecraftServer server) {
        wrapper.stopScheduler();
    }

    @Override
    public void onServerStarted(MinecraftServer server) {
        wrapper.createScheduler();
    }
    
    private SchedulerWrapper wrapper;

    public TaskCleanup(SchedulerWrapper wrapper) {
        this.wrapper = wrapper;
    }
}