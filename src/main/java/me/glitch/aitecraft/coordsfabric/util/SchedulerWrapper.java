package me.glitch.aitecraft.coordsfabric.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class SchedulerWrapper {
    public ScheduledExecutorService scheduler;
    private boolean created = false;

    public void stopScheduler() {
        scheduler.shutdown();
        try {
            boolean properlyTerminated = scheduler.awaitTermination(2, TimeUnit.SECONDS);
            if (!properlyTerminated) {
                System.out.println("[Coords] [ERROR] Scheduler thread failed to stop in 2 seconds.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createScheduler() {
        if (created && !scheduler.isTerminated()) {
            this.stopScheduler();
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

}