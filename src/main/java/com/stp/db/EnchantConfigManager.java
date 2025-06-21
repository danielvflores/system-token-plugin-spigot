package com.stp.db;

import org.bukkit.plugin.java.JavaPlugin;

public class EnchantConfigManager {

    private final JavaPlugin plugin;

    public EnchantConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public int getMaxLevel(String enchantName) {
        return plugin.getConfig().getInt("enchants." + enchantName.toLowerCase() + ".max-level", 1);
    }

    public String getDisplayName(String enchantName) {
        return plugin.getConfig().getString("enchants." + enchantName.toLowerCase() + ".display", enchantName);
    }

    public boolean isEnabled(String enchantName) {
        return plugin.getConfig().getBoolean("enchants." + enchantName.toLowerCase() + ".enabled", false);
    }

    public String getMessage(String key) {
        return plugin.getConfig().getString("messages." + key, "Mensaje no configurado: " + key);
    }
}
