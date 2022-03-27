package com.gabrielhd.partycrackers.Crackers;

import com.gabrielhd.partycrackers.Main;
import com.gabrielhd.partycrackers.Utils.ItemBuilder;
import com.gabrielhd.partycrackers.Utils.SoundUtils;
import com.gabrielhd.partycrackers.Utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Map;

public class CrackerTime {

    private final Main plugin;

    @Getter @Setter private int time;
    @Getter private final Cracker cracker;
    @Getter private final Item item;

    public CrackerTime(Main plugin, Item item, Cracker cracker) {
        this.plugin = plugin;

        this.cracker = cracker;
        this.item = item;

        this.time = cracker.getExplosionTime() * 20;
    }

    public boolean check() {
        if(this.time-- == 0) {
            this.item.remove();

            ParticleBuilder builder = new ParticleBuilder(ParticleEffect.valueOf(this.cracker.getEffectExplode()));
            builder.setAmount(this.cracker.getAmountEffectExplode());
            builder.setSpeed(this.cracker.getSpeedEffectExplode());
            builder.setLocation(this.item.getLocation());
            builder.display();

            if(this.cracker.isStrikeLightningEffect()) this.item.getLocation().getWorld().strikeLightningEffect(this.item.getLocation());
            this.item.getLocation().getWorld().playSound(this.item.getLocation(), SoundUtils.getSound(this.cracker.getSoundExplode(), Sound.ENTITY_GENERIC_EXPLODE), 2f, 2f);

            this.dropRewards();
            return true;
        }

        Location location = Utils.getRandomLocation(this.item.getLocation(), this.cracker.getExplosionRange(), this.cracker.isExplosionSphere(), this.cracker.isSphereHollow());

        ParticleBuilder builder = new ParticleBuilder(ParticleEffect.valueOf(this.cracker.getEffectExploding()));
        builder.setAmount(this.cracker.getAmountEffectExploding());
        builder.setSpeed(this.cracker.getSpeedEffectExploding());
        builder.setLocation(location);
        builder.display();

        location.getWorld().playSound(location, SoundUtils.getSound(this.cracker.getSoundExploding(), Sound.ENTITY_TNT_PRIMED), 2f, 2f);
        return false;
    }

    public void dropRewards() {
        for(Map.Entry<ItemBuilder, Double> value : this.cracker.getRewards().entrySet()) {
            ItemBuilder itemBuilder = value.getKey();

            if(!Utils.getChance(value.getValue())) continue;

            for(int i = 0; i < itemBuilder.getAmount(); i++) {
                Location location = Utils.getRandomLocation(this.item.getLocation(), this.cracker.getExplosionRange(), true, false);

                ItemBuilder itemBuilder2 = itemBuilder.clone();
                itemBuilder2.setAmount(1);

                location.getWorld().dropItemNaturally(location, itemBuilder2.build());
            }
        }
    }
}
