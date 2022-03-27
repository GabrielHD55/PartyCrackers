package com.gabrielhd.partycrackers.Manager;

import com.gabrielhd.partycrackers.Config.YamlConfig;
import com.gabrielhd.partycrackers.Crackers.Cracker;
import com.gabrielhd.partycrackers.Crackers.CrackerTime;
import com.gabrielhd.partycrackers.Main;
import com.gabrielhd.partycrackers.Utils.ItemBuilder;
import com.gabrielhd.partycrackers.Utils.Utils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrackerManager {

    @Getter private final Map<String, Cracker> crackers;
    @Getter private final List<CrackerTime> crackerTimes;

    private final Main plugin;

    public CrackerManager(Main plugin) {
        this.plugin = plugin;

        this.crackers = Maps.newHashMap();
        this.crackerTimes = Lists.newArrayList();

        this.loadCrackers();
    }

    public Cracker getCracker(String name) {
        return this.crackers.get(name.toLowerCase());
    }

    public Cracker getCracker(ItemStack itemStack) {
        for(Cracker cracker : this.crackers.values()) {
            if(cracker.getItemBuilder().build().isSimilar(itemStack)) {
                return cracker;
            }
        }

        return null;
    }

    public void loadCrackers() {
        this.crackers.clear();

        YamlConfig settings = this.plugin.getConfigManager().getSettings();

        Set<String> sections = settings.getConfigurationSection("Crackers").getKeys(false);
        for(String sec : sections) {
            String path = "Crackers."+sec+".Item.";

            ItemBuilder builder = new ItemBuilder(settings.getString(path+"ID"));
            builder.setName(settings.getString(path+"Name"));
            builder.setAmount(settings.getInt(path+"Amount", 1));
            builder.setGlow(settings.getBoolean(path+"Glow", false));
            builder.setLore(settings.getStringList(path+"Lore"));

            for(String enchant : settings.getStringList(path+"Enchants")) {
                String enchantName;
                int enchantLevel;

                if(!enchant.isEmpty()) {
                    if (enchant.contains(":")) {
                        String[] split = enchant.split(":");

                        enchantName = split[0];
                        enchantLevel = Utils.parseInt(split[1]);
                    } else {
                        enchantName = enchant;
                        enchantLevel = 1;
                    }

                    Enchantment enchantment = Utils.getEnchant(enchantName);
                    if(enchantment != null) {
                        builder.addEnchant(enchantment, enchantLevel);
                    }
                }
            }

            this.crackers.put(sec.toLowerCase(), new Cracker(builder, settings.getConfigurationSection("Crackers." + sec)));
        }

        this.plugin.getLogger().info(this.crackers.size()+" PartyCrackers loaded!");
    }
}
