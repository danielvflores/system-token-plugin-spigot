package com.stp.utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.core.SystemTokenEnchant;

public class PlaceholderUtil {

    // Método principal con Map
    public static String applyPlaceholders(Player player, String text, Map<String, String> extraPlaceholders) {
        if (text == null) return null;
        if (player == null) return text;

        // %player%
        text = text.replace("%player%", player.getName());

        // %token_balance%
        text = text.replace("%token_balance%", SystemTokenEnchant.getInstance().getTokenManager().getTokens(player.getUniqueId()).toPlainString());

        // %token_balance_formatted%
        BigDecimal tokens = SystemTokenEnchant.getInstance().getTokenManager().getTokens(player.getUniqueId());
        String formatted = NumberUtils.formatWithSuffix(tokens);
        text = text.replace("%token_balance_formatted%", formatted != null ? formatted : "0");

        // %enchant_<enchant>_<suffix>%
        Pattern enchantPattern = Pattern.compile("%enchant_([a-zA-Z0-9_]+)_(current_level|next_level|max_level|name|cost_per_level)%");
        Matcher matcher = enchantPattern.matcher(text);
        while (matcher.find()) {
            String enchantName = matcher.group(1);
            String suffix = matcher.group(2);
            String replacement = "";

            int currentLevel = SystemTokenEnchant.getInstance().getEnchantmentManager().getCurrentLevel(player, enchantName);
            int maxLevel = SystemTokenEnchant.getInstance().getEnchantmentManager().getMaxLevel(enchantName);

            switch (suffix) {
                case "current_level":
                    replacement = String.valueOf(Math.max(currentLevel, 0));
                    break;
                case "next_level":
                    replacement = String.valueOf(currentLevel >= maxLevel ? maxLevel : currentLevel + 1);
                    break;
                case "max_level":
                    replacement = String.valueOf(maxLevel);
                    break;
                case "name":
                    String name = SystemTokenEnchant.getInstance().getEnchantmentManager().getEnchantmentName(enchantName);
                    replacement = name.replaceAll("§[0-9a-fk-or]|&[0-9a-fk-or]", ""); // Elimina colores
                    break;
                case "cost_per_level":
                    replacement = SystemTokenEnchant.getInstance().getEnchantmentManager().getCurrentCostFormatted(player, enchantName);
                    break;
            }
            text = text.replace(matcher.group(0), replacement);
        }

        // %pickaxe_name% y %pickaxe_lore%
        ItemStack item = player.getInventory().getItemInHand();
        if (item != null && item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
                text = text.replace("%pickaxe_name%", item.getItemMeta().getDisplayName());
            }
            if (item.getItemMeta().hasLore()) {
                text = text.replace("%pickaxe_lore%", String.join("\n", item.getItemMeta().getLore()));
            }
        } else {
            text = text.replace("%pickaxe_name%", "");
            text = text.replace("%pickaxe_lore%", "");
        }

        if (extraPlaceholders != null) {
            for (Map.Entry<String, String> entry : extraPlaceholders.entrySet()) {
                text = text.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }

        return text;
    }
    public static String applyPlaceholders(Player player, String text) {
        return applyPlaceholders(player, text, null);
    }
}