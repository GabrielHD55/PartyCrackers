package com.gabrielhd.partycrackers.Manager;

import com.gabrielhd.partycrackers.Config.YamlConfig;
import com.gabrielhd.partycrackers.Main;
import lombok.Getter;

public class ConfigManager {

    @Getter private final YamlConfig settings;
    @Getter private final YamlConfig messages;

    public ConfigManager(Main plugin) {
        this.settings = new YamlConfig(plugin, "Settings");
        this.messages = new YamlConfig(plugin, "Messages");
    }

    public boolean reload() {
        return this.settings.reload() && this.messages.reload();
    }
}
