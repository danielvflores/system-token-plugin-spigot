package com.stp.enchants.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;

public class Fly implements CustomEnchant {

    private final int level;
    private final String displayName;
    private final String pickaxeName;
    private final int maxLevel;
    private final boolean enabled;

    public Fly(int level) {
        this.level = level;
        this.displayName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("enchants.fly.display", "&7Fly");
        this.maxLevel = PrisonEnchantCustom.getInstance().getConfig()
                .getInt("enchants.fly.max-level", 1);
        this.enabled = PrisonEnchantCustom.getInstance().getConfig()
                .getBoolean("enchants.fly.enabled", true);
        this.pickaxeName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("pickaxe.display-name", "&f&lPICO &7&l| &a&lINICIAL");
    }

    @Override
    public String getId() {
        return "fly";
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void onEnable(Player player, int level) {
        if (!enabled) return;
        player.setAllowFlight(true);
    }

    @Override
    public void onDisable(Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
    }

    @Override
    public void applyEffect(Player player, int level) {
        if (!enabled) return;
        player.setAllowFlight(true);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return enabled && item != null && item.getType().toString().contains(pickaxeName);
    }
}