package com.gabrielhd.partycrackers.Config;

import com.gabrielhd.partycrackers.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class YamlConfig extends YamlConfiguration {

    private final File file;
    private final String path;
    private final Main plugin;

    public YamlConfig(Main plugin, String path) {
        this.plugin = plugin;

        this.path = path+".yml";
        this.file = new File(plugin.getDataFolder(), this.path);

        this.options().parseComments(true);
        this.saveDefault();
        this.reload();
    }

    public boolean reload() {
        try {
            super.load(this.file);
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load "+this.path+" file, please check console!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean save() {
        try {
            super.save(this.file);
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to save "+this.path+" file, please check console!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void saveDefault() {
        try {
            if(!this.file.exists()) {
                if(this.plugin.getResource(this.path) != null) {
                    this.plugin.saveResource(this.path, false);
                } else {
                    this.file.createNewFile();
                }
            }
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to create "+this.path+" file, please check console");
            e.printStackTrace();
        }
    }
}
