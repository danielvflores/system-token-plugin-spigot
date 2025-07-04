package com.stp.enchants.impl;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.core.SystemTokenEnchant;
import com.stp.enchants.CustomEnchant;

public class Fly implements CustomEnchant {

    private final int level;
    private final String displayName;
    private final String pickaxeName;
    private final int maxLevel;
    private final boolean enabled;

    public Fly(int level) {
        this.level = level;
        this.displayName = SystemTokenEnchant.getInstance().getConfig()
                .getString("enchants.fly.display", "&7Fly");
        this.maxLevel = SystemTokenEnchant.getInstance().getConfig()
                .getInt("enchants.fly.max-level", 1);
        this.enabled = SystemTokenEnchant.getInstance().getConfig()
                .getBoolean("enchants.fly.enabled", true);
        this.pickaxeName = SystemTokenEnchant.getInstance().getConfig()
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
}