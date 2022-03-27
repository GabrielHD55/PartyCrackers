package com.gabrielhd.partycrackers.Crackers;

import com.gabrielhd.partycrackers.Utils.ItemBuilder;
import com.gabrielhd.partycrackers.Utils.Utils;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;
import java.util.Set;

public class Cracker {

    @Getter private final String name;
    @Getter private final String displayName;
    @Getter private final ItemBuilder itemBuilder;

    @Getter private final int explosionTime;
    @Getter private final int explosionRange;
    @Getter private final boolean sphereHollow;
    @Getter private final boolean explosionSphere;
    @Getter private final boolean strikeLightningEffect;

    @Getter private final String soundExploding;
    @Getter private final String effectExploding;
    @Getter private final int speedEffectExploding;
    @Getter private final int amountEffectExploding;

    @Getter private final String soundExplode;
    @Getter private final String effectExplode;
    @Getter private final int speedEffectExplode;
    @Getter private final int amountEffectExplode;

    @Getter private final Map<ItemBuilder, Double> rewards;

    public Cracker(ItemBuilder itemBuilder, ConfigurationSection config) {
        this.itemBuilder = itemBuilder;

        this.name = config.getName();
        this.displayName = config.getString("DisplayName");
        this.explosionTime = config.getInt("ExplosionTime", 10);
        this.explosionRange = config.getInt("ExplosionRange", 3);
        this.explosionSphere = config.getBoolean("ExplosionSpHere", true);
        this.sphereHollow = config.getBoolean("SphereHollow", false);
        this.strikeLightningEffect = config.getBoolean("StrikeLightningEffect", true);

        this.soundExploding = config.getString("Effects.Exploding.Sound", "ENTITY_TNT_PRIMED");
        this.effectExploding = config.getString("Effects.Exploding.Effect", "FLAME");
        this.speedEffectExploding = config.getInt("Effects.Exploding.Speed", 1);
        this.amountEffectExploding = config.getInt("Effects.Exploding.Amount", 5);

        this.soundExplode = config.getString("Effects.Explode.Sound", "ENTITY_GENERIC_EXPLODE");
        this.effectExplode = config.getString("Effects.Explode.Effect", "EXPLOSION_LARGE");
        this.speedEffectExplode = config.getInt("Effects.Explode.Speed", 1);
        this.amountEffectExplode = config.getInt("Effects.Explode.Amount", 5);

        this.rewards = Maps.newHashMap();

        this.loadRewards(config);
    }

    private void loadRewards(ConfigurationSection config) {
        Set<String> sections = config.getConfigurationSection("Rewards").getKeys(false);
        for(String reward : sections) {
            String path = "Rewards." + reward + ".";

            ItemBuilder builder = new ItemBuilder(config.getString(path + "ID"));
            builder.setName(config.getString(path+"Name"));
            builder.setAmount(config.getInt(path+"Amount", 1));
            builder.setGlow(config.getBoolean(path+"Glow", false));
            builder.setLore(config.getStringList(path+"Lore"));

            for(String enchant : config.getStringList(path+"Enchants")) {
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

            this.rewards.put(builder, config.getDouble(path + "Chance", 50));
        }
    }
}
