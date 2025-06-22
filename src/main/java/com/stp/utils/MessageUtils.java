package com.stp.utils;

import org.bukkit.ChatColor;

import com.stp.core.PrisonEnchantCustom;

public class MessageUtils {
    public static String getMessage(String key) {
        String prefix = PrisonEnchantCustom.getInstance().getConfig().getString("message-prefix", "");
        String msg = PrisonEnchantCustom.getInstance().getConfig().getString("messages." + key, "&cMensaje no encontrado: " + key);
        return ChatColor.translateAlternateColorCodes('&', prefix + msg);
    }

    public static String getMessage(String key, String... replacements) {
        String msg = getMessage(key);
        for (int i = 0; i < replacements.length - 1; i += 2) {
            msg = msg.replace(replacements[i], replacements[i + 1]);
        }
        return msg;
    }
}