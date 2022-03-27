package com.gabrielhd.partycrackers.Utils;

import org.bukkit.Sound;

public class SoundUtils {

    public static Sound getSound(String sound, Sound defaultSound) {
        for(Sound sounds : Sound.values()) {
            if(sounds.name().equalsIgnoreCase(sound) || sounds.getKey().getKey().equalsIgnoreCase(sound)) {
                return sounds;
            }
        }

        return defaultSound;
    }
}
