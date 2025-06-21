package com.stp.utils;

import java.util.Map;

import org.bukkit.entity.Player;

public class PlaceholderUtil {
    public static String applyPlaceholders(Player player, String text, Map<String, String> extraPlaceholders) {
        if (player != null) {
            text = text.replace("%player%", player.getName());
        }

        for (Map.Entry<String, String> entry : extraPlaceholders.entrySet()) {
            text = text.replace("%" + entry.getKey() + "%", entry.getValue());
        }

        return text;
    }
}
