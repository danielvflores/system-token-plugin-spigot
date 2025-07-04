package com.stp.objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomItem {
    ItemStack create(Player player);
    boolean isCustomItem(ItemStack item);
    ItemStack addCustomEnchantment(ItemStack item, com.stp.enchants.CustomEnchant enchant, Player player);
    int getCustomEnchantmentLevel(ItemStack item, String enchantId);
    com.stp.enchants.CustomEnchant getCustomEnchantment(ItemStack item, String enchantId);
    ItemStack removeCustomEnchantment(ItemStack item, String enchantId, Player player);
    void refreshLore(ItemStack item, Player player);

}