package com.stp.enchants.impl;

import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;

public class Fortune implements CustomEnchant {
    private final int level;
    private final String displayName;
    private final String displayNameRaw;
    private final int maxLevel;
    private final boolean enabled;

    public Fortune(int level) {
        this.level = level;

        String coloredName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("enchants.fortune.display", "&7Fortune");
        this.displayName = coloredName;
        this.displayNameRaw = stripColorCodes(coloredName);
        this.maxLevel = PrisonEnchantCustom.getInstance().getConfig()
                .getInt("enchants.fortune.max-level", 100);
        this.enabled = PrisonEnchantCustom.getInstance().getConfig()
                .getBoolean("enchants.fortune.enabled", true);
    }

    @Override
    public String getId() {
        return "fortune";
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

        List<String> allowedTypes = PrisonEnchantCustom.getInstance().getConfig()
            .getStringList("enchants." + getId() + ".enchants-item-avaible");
        boolean strict = PrisonEnchantCustom.getInstance().getConfig()
            .getBoolean("enchants." + getId() + ".enchant-strict", false);

        String typeName = item.getType().name();

        boolean typeAllowed = allowedTypes.stream().anyMatch(typeName::endsWith);
        if (!typeAllowed) return false;

        if (strict) {

            String requiredName = PrisonEnchantCustom.getInstance().getConfig()
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
            meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, effectiveLevel, true);
            item.setItemMeta(meta);
        }
    }

    private void removeVanillaEnchant(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
            meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
            item.setItemMeta(meta);
        }
    }

    private String stripColorCodes(String input) {
        return input.replaceAll("&[0-9a-fk-or]", "");
    }
}