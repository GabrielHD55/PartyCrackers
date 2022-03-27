package com.gabrielhd.partycrackers.Utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class ItemBuilder {

    @Getter @Setter private String material;
    @Getter @Setter private String name;

    @Getter @Setter private int amount;
    @Getter @Setter private boolean glow;

    @Getter private final List<String> lore;
    @Getter private Map<Enchantment, Integer> enchants;

    public ItemBuilder() {
        this.amount = 1;
        this.glow = false;

        this.lore = Lists.newArrayList();
        this.enchants = Maps.newHashMap();
    }

    public ItemBuilder(String material) {
        this.material = material;
        this.amount = 1;
        this.glow = false;

        this.lore = Lists.newArrayList();
        this.enchants = Maps.newHashMap();
    }

    public void setLore(List<String> lores) {
        this.lore.clear();

        for(String lore : lores) {
            this.lore.add(Utils.Color(lore));
        }
    }

    public ItemBuilder clone() {
        ItemBuilder itemBuilder = new ItemBuilder();
        itemBuilder.setName(this.name);
        itemBuilder.setGlow(this.glow);
        itemBuilder.setLore(this.lore);
        itemBuilder.setAmount(this.amount);
        itemBuilder.setMaterial(this.material);
        itemBuilder.setEnchants(this.enchants);

        return itemBuilder;
    }

    public void setEnchants(Map<Enchantment, Integer> enchant) {
        this.enchants = enchant;
    }

    public void addEnchant(Enchantment enchantment, int level) {
        this.enchants.put(enchantment, level);
    }

    public ItemStack build() {
        Material mat = Utils.getMaterial(this.material);

        ItemStack itemStack = new ItemStack(mat, this.amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(this.name != null && !this.name.isEmpty() && !this.name.isBlank()) {
            itemMeta.setDisplayName(Utils.Color(this.name));
        }

        if(!this.lore.isEmpty()) {
            itemMeta.setLore(this.lore);
        }

        itemStack.setItemMeta(itemMeta);

        if(!this.enchants.isEmpty()) {
            for(Map.Entry<Enchantment, Integer> value : this.enchants.entrySet()) {
                itemStack.addUnsafeEnchantment(value.getKey(), value.getValue());
            }
        }

        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if(this.glow) {
            NBTTagCompound tag = nmsItem.t();
            tag.a("Enchantments", nmsItem.u());
            nmsItem.c(tag);
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
