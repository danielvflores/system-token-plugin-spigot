package com.stp.utils;
import java.math.BigDecimal;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.core.PrisonEnchantCustom;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class STPExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "stp";
    }

    @Override
    public String getAuthor() {
        return "danielvflores";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";

        // %stp_token_balance% - Returns the player's token balance as a string (1, 10, 100, 1000, 100000000).
        if (identifier.equalsIgnoreCase("token_balance")) {
            return PrisonEnchantCustom.getInstance().getTokenManager().getTokens(player.getUniqueId()).toPlainString();
        }

        // %stp_token_balance_formatted% - Returns the player's token balance formatted with suffixes (1, 10, 100, 1K, 10K, 100K, 1M, etc.).
        if (identifier.equalsIgnoreCase("token_balance_formatted")) {
            BigDecimal tokens = PrisonEnchantCustom.getInstance().getTokenManager().getTokens(player.getUniqueId());
            String formatted = NumberUtils.formatWithSuffix(tokens);
            return formatted != null ? formatted : "0";
        }

        // %stp_enchant_<enchant>_<suffix>%
        if (identifier.startsWith("enchant_")) {
            String suffix = null;
            if (identifier.endsWith("_current_level")) suffix = "_current_level"; // Returns the current level of the enchantment.
            else if (identifier.endsWith("_next_level")) suffix = "_next_level"; // Returns the next level of the enchantment, or the max level if the current level is already at max.
            else if (identifier.endsWith("_max_level")) suffix = "_max_level"; // Returns the max level of the enchantment.
            else if (identifier.endsWith("_name")) suffix = "_name"; // Returns the name of the enchantment.

            if (suffix != null) {
                String enchantName = identifier.substring(8, identifier.length() - suffix.length());
                int currentLevel = PrisonEnchantCustom.getInstance().getEnchantmentManager().getCurrentLevel(player, enchantName);
                int maxLevel = PrisonEnchantCustom.getInstance().getEnchantmentManager().getMaxLevel(enchantName);

                switch (suffix) {
                    case "_current_level":
                        return String.valueOf(Math.max(currentLevel, 0));
                    case "_next_level":
                        return String.valueOf(currentLevel >= maxLevel ? maxLevel : currentLevel + 1);
                    case "_max_level":
                        return String.valueOf(maxLevel);
                    case "_name":
                        return PrisonEnchantCustom.getInstance().getEnchantmentManager().getEnchantmentName(enchantName);
                }
            }
        }

        // %stp_pickaxe_<suffix>%
        if (identifier.startsWith("pickaxe_")) {
            String suffix = null;
            if (identifier.endsWith("_name")) suffix = "_name";
            else if (identifier.endsWith("_lore")) suffix = "_lore";

            if (suffix != null) {
                ItemStack item = player.getInventory().getItemInHand();
                if (item != null && item.hasItemMeta()) {
                    switch (suffix) {
                        case "_name":
                            if (item.getItemMeta().hasDisplayName()) {
                                return item.getItemMeta().getDisplayName();
                            }
                            return "";
                        case "_lore":
                            if (item.getItemMeta().hasLore()) {
                                return String.join("\n", item.getItemMeta().getLore());
                            }
                            return "";
                    }
                }
                return "";
            }
        }

        return null;
    }
}