package com.stp.enchants.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;

public class Speed implements CustomEnchant {
    private final int level;
    private final String displayName;
    private final String pickaxeName;
    private final int maxLevel;
    private final boolean enabled;

    public Speed(int level) {
        this.level = level;
        this.displayName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("enchants.speed.display", "Velocidad");
        this.maxLevel = PrisonEnchantCustom.getInstance().getConfig()
                .getInt("enchants.speed.max-level", 3);
        this.enabled = PrisonEnchantCustom.getInstance().getConfig()
                .getBoolean("enchants.speed.enabled", true);
        this.pickaxeName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("pickaxe.display-name", "&f&lPICO &7&l| &a&lINICIAL");
    }

    @Override
    public String getId() {
        return "speed";
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
        applyEffect(player, level);
    }

    @Override
    public void onDisable(Player player) {
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public void applyEffect(Player player, int level) {
        if (!enabled) return;
        player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(new PotionEffect(
            PotionEffectType.SPEED, Integer.MAX_VALUE, level - 1, true, false));
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return enabled && item != null && item.getType().toString().contains(pickaxeName);
    }
}