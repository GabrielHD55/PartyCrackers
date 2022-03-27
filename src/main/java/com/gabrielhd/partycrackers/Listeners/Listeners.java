package com.gabrielhd.partycrackers.Listeners;

import com.gabrielhd.partycrackers.Crackers.Cracker;
import com.gabrielhd.partycrackers.Crackers.CrackerTime;
import com.gabrielhd.partycrackers.Main;
import com.gabrielhd.partycrackers.Manager.CrackerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

    private final Main plugin;

    public Listeners(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemDrop().getItemStack();

        CrackerManager crackerManager = this.plugin.getCrackerManager();

        Cracker cracker = crackerManager.getCracker(itemStack);
        if(cracker != null) {
            crackerManager.getCrackerTimes().add(new CrackerTime(this.plugin, event.getItemDrop(), cracker));
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem().getItemStack();

        CrackerManager crackerManager = this.plugin.getCrackerManager();
        Cracker cracker = crackerManager.getCracker(itemStack);
        if(cracker != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMerge(ItemMergeEvent event) {
        ItemStack itemStack = event.getEntity().getItemStack();

        CrackerManager crackerManager = this.plugin.getCrackerManager();
        Cracker cracker = crackerManager.getCracker(itemStack);
        if(cracker != null) {
            event.setCancelled(true);
        }
    }
}
