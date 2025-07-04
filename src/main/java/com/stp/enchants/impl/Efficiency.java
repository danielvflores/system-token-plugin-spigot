package com.stp.enchants.impl;

import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.stp.core.SystemTokenEnchant;
import com.stp.enchants.CustomEnchant;

public class Efficiency implements CustomEnchant {
    private final int level;
    private final String displayName;
    private final String displayNameRaw;
    private final int maxLevel;
    private final boolean enabled;

    public Efficiency(int level) {
        this.level = level;

        String coloredName = SystemTokenEnchant.getInstance().getConfig()
                .getString("enchants.efficiency.display", "&7Efficiency");
        this.displayName = coloredName;
        this.displayNameRaw = stripColorCodes(coloredName);
        this.maxLevel = SystemTokenEnchant.getInstance().getConfig()
                .getInt("enchants.efficiency.max-level", 100);
        this.enabled = SystemTokenEnchant.getInstance().getConfig()
                .getBoolean("enchants.efficiency.enabled", true);
    }

    @Override
    public String getId() {
        return "efficiency";
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayNameRaw() {
        return displayNameRaw;
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
        ItemStack item = player.getInventory().getItemInHand();
        if (canEnchantItem(item)) {
            removeVanillaEnchant(item);
        }
    }

    @Override
    public void applyEffect(Player player, int level) {
        ItemStack item = player.getInventory().getItemInHand();
        if (canEnchantItem(item)) {
            applyVanillaEnchant(item, level);
        }
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        if (!enabled || item == null) return false;

        List<String> allowedTypes = SystemTokenEnchant.getInstance().getConfig()
            .getStringList("enchants." + getId() + ".enchants-item-avaible");
        boolean strict = SystemTokenEnchant.getInstance().getConfig()
            .getBoolean("enchants." + getId() + ".enchant-strict", false);

        String typeName = item.getType().name();

        boolean typeAllowed = false;
        for (String allowedType : allowedTypes) {
            if (typeName.endsWith(allowedType)) {
                typeAllowed = true;
                break;
            }
        }
        if (!typeAllowed) return false;

        if (strict) {

            String requiredName = SystemTokenEnchant.getInstance().getConfig()
                .getString("pickaxe.display-name", "");
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
            String displayName = item.getItemMeta().getDisplayName();

            return displayName.equals(requiredName.replace("&", "§"));
        }

        return true;
    }

    private void applyVanillaEnchant(ItemStack item, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            int effectiveLevel = level;
            meta.addEnchant(Enchantment.DIG_SPEED, effectiveLevel, true);
            item.setItemMeta(meta);
        }
    }

    private void removeVanillaEnchant(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasEnchant(Enchantment.DIG_SPEED)) {
            meta.removeEnchant(Enchantment.DIG_SPEED);
            item.setItemMeta(meta);
        }
    }

    private String stripColorCodes(String input) {
        return input.replaceAll("&[0-9a-fk-or]", "");
    }
}
