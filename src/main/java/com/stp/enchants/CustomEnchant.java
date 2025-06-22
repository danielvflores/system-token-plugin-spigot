package com.stp.enchants;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomEnchant {
    String getId();
    String getDisplayName();
    int getMaxLevel();
    void onEnable(Player player, int level);
    void onDisable(Player player);
    void applyEffect(Player player, int level);
    boolean canEnchantItem(ItemStack item);

    public int getLevel();
}