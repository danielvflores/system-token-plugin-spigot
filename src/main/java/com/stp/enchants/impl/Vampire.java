package com.stp.enchants.impl;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.core.SystemTokenEnchant;
import com.stp.enchants.CustomEnchant;

public class Vampire implements CustomEnchant {
    private final int level;
    private final String displayName;
    private final int maxLevel;
    private final boolean enabled;

    public Vampire(int level) {
        this.level = level;
        this.displayName = SystemTokenEnchant.getInstance().getConfig()
                .getString("enchants.vampire.display", "Vampiro");
        this.maxLevel = SystemTokenEnchant.getInstance().getConfig()
                .getInt("enchants.vampire.max-level", 3);
        this.enabled = SystemTokenEnchant.getInstance().getConfig()
                .getBoolean("enchants.vampire.enabled", true);
    }

    @Override
    public String getId() {
        return "vampire";
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
        // No necesitamos remover efectos ya que el vampirismo funciona por golpe
    }

    @Override
    public void applyEffect(Player player, int level) {
        // Este método se llama cuando se equipa el item
        // El efecto real de vampirismo se maneja en el listener de daño
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
            String itemDisplayName = item.getItemMeta().getDisplayName();

            return itemDisplayName.equals(requiredName.replace("&", "§"));
        }

        return true;
    }
    
}
