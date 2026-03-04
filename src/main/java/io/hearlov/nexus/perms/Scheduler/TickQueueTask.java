package io.hearlov.nexus.perms.Scheduler;

import cn.nukkit.scheduler.Task;
import io.hearlov.nexus.perms.Cache.PlayerCache;

public class TickQueueTask extends Task{

    @Override
    public void onRun(int currentTick){
        PlayerCache.tickQueue();
    }
}
