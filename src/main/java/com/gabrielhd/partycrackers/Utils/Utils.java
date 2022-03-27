package com.gabrielhd.partycrackers.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    private static final Random random =  new Random();

    public static String Color(String s) {
        return s.replaceAll("&", "§");
    }

    public static Enchantment getEnchant(String enchantName) {
        for(Enchantment enchant : Enchantment.values()) {
            if(enchant.getName().equalsIgnoreCase(enchantName) || enchant.getKey().getKey().equalsIgnoreCase(enchantName)) {
                return enchant;
            }
        }

        return null;
    }

    public static Material getMaterial(String material) {
        for(Material materials : Material.values()) {
            if(materials.name().equalsIgnoreCase(material)) {
                return materials;
            }
        }

        return Material.BEDROCK;
    }

    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));
                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        circleBlocks.add(new Location(centerBlock.getWorld(), x, y, z));
                    }
                }
            }
        }

        return circleBlocks;
    }

    public static Location getRandomLocation(Location origin, double radius, boolean sphere, boolean hollow) {
        if(sphere) {
            List<Location> locations = generateSphere(origin, (int) radius, hollow);

            return locations.get(random.nextInt(locations.size() - 1));
        }

        Location location = new Location(origin.getWorld(), 0, 0, 0);
        location.setX(origin.getX() + Math.random() * radius * 2 - radius);
        location.setZ(origin.getZ() + Math.random() * radius * 2 - radius);

        location.setY(origin.getWorld().getHighestBlockAt(location.getBlockX(), location.getBlockZ()).getY() + 0.5 + random.nextInt((int) radius));

        return location;
    }

    public static boolean getChance(double chance) {
        return chance < Math.random() * 100.0;
    }
}
