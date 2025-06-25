package com.stp.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.stp.core.PrisonEnchantCustom;
import com.stp.utils.PlaceholderUtil;

public class EnchantGUI {
    public static Inventory createEnchantGUI(Player player) {
        ConfigurationSection guiSection = PrisonEnchantCustom.getInstance().getConfig().getConfigurationSection("enchant-gui");
        if (guiSection == null) {
            return Bukkit.createInventory(null, 54, "Enchant Menu");
        }

        String title = guiSection.getString("title", "Enchant Menu").replace("&", "§");
        int size = guiSection.getInt("size", 54);

        if (size < 9) size = 9;
        if (size > 54) size = 54;
        if (size % 9 != 0) size = 54;

        Inventory inv = Bukkit.createInventory(null, size, title);

        ConfigurationSection itemsSection = guiSection.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSec = itemsSection.getConfigurationSection(key);
                if (itemSec == null) continue;

                int slot = itemSec.getInt("slot", -1);
                if (slot < 0 || slot >= size) continue;

                String materialName = itemSec.getString("material", "STONE");
                Material material = Material.getMaterial(materialName.toUpperCase());
                if (material == null) material = Material.STONE;

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();

                if (itemSec.contains("name")) {
                    String rawName = itemSec.getString("name").replace("&", "§");
                    meta.setDisplayName(PlaceholderUtil.applyPlaceholders(player, rawName));
                }
 
                if (itemSec.contains("lore")) {
                    List<String> lore = itemSec.getStringList("lore");
                    java.util.List<String> finalLore = new java.util.ArrayList<>();
                    for (String line : lore) {
                        String rawLore = line.replace("&", "§");
                        if (rawLore.equals("%pickaxe_lore%")) {
                            ItemStack hand = player.getInventory().getItemInHand();
                            if (hand != null && hand.hasItemMeta() && hand.getItemMeta().hasLore()) {
                                for (String pickaxeLoreLine : hand.getItemMeta().getLore()) {
                                    finalLore.add(pickaxeLoreLine);
                                }
                            }
                        } else {
                            finalLore.add(PlaceholderUtil.applyPlaceholders(player, rawLore));
                        }
                    }
                    meta.setLore(finalLore);
                }

                item.setItemMeta(meta);
                inv.setItem(slot, item);
            }
        }

        return inv;
    }
}