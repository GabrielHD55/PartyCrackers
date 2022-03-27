package com.gabrielhd.partycrackers;

import com.gabrielhd.partycrackers.Commands.Commands;
import com.gabrielhd.partycrackers.Listeners.Listeners;
import com.gabrielhd.partycrackers.Manager.ConfigManager;
import com.gabrielhd.partycrackers.Manager.CrackerManager;
import com.gabrielhd.partycrackers.Tasks.CrackerTask;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter private ConfigManager configManager;
    @Getter private CrackerManager crackerManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.crackerManager = new CrackerManager(this);

        this.getCommand("partycrackers").setExecutor(new Commands(this));
        this.getCommand("partycrackers").setTabCompleter(new Commands(this));

        this.getServer().getPluginManager().registerEvents(new Listeners(this), this);

        this.getServer().getScheduler().runTaskTimer(this, new CrackerTask(this), 0L, 1L);
    }
}
