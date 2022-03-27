package com.gabrielhd.partycrackers.Tasks;

import com.gabrielhd.partycrackers.Crackers.CrackerTime;
import com.gabrielhd.partycrackers.Main;
import com.gabrielhd.partycrackers.Manager.CrackerManager;

public class CrackerTask implements Runnable {

    private final Main plugin;
    private final CrackerManager crackerManager;

    public CrackerTask(Main plugin) {
        this.plugin = plugin;

        this.crackerManager = this.plugin.getCrackerManager();
    }

    @Override
    public void run() {
        if(this.crackerManager.getCrackerTimes().isEmpty()) return;

        this.crackerManager.getCrackerTimes().removeIf(CrackerTime::check);
    }
}
