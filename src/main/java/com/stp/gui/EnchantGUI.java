package com.stp.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.stp.core.SystemTokenEnchant;

public class EnchantGUI {
    private static final int[] ENCHANT_SLOTS = {12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34};

    public static Inventory createEnchantGUI(Player player) {
        return createEnchantGUI(player, 0);
    }

    public static Inventory createEnchantGUI(Player player, int page) {
        ConfigurationSection guiSection = SystemTokenEnchant.getInstance().getConfig().getConfigurationSection("enchant-gui");
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
        ItemStack hand = player.getInventory().getItemInHand();

        // 1. Filtrar los encantamientos compatibles
        List<String> compatibleEnchants = new ArrayList<>();
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                if (SystemTokenEnchant.getInstance().getConfig().isConfigurationSection("enchants." + key)) {
                    List<String> allowedTypes = SystemTokenEnchant.getInstance().getConfig()
                        .getStringList("enchants." + key + ".enchants-item-avaible");
                    boolean strict = SystemTokenEnchant.getInstance().getConfig()
                        .getBoolean("enchants." + key + ".enchant-strict", false);

                    String typeName = hand != null ? hand.getType().name() : "";
                    boolean typeAllowed = allowedTypes.stream().anyMatch(typeName::endsWith);

                    boolean show = false;
                    if (typeAllowed) {
                        if (strict) {
                            String requiredName = SystemTokenEnchant.getInstance().getConfig()
                                .getString("pickaxe.display-name", "");
                            if (hand != null && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
                                String displayName = hand.getItemMeta().getDisplayName();
                                show = displayName.equals(requiredName.replace("&", "§"));
                            }
                        } else {
                            show = true;
                        }
                    }
                    if (show) compatibleEnchants.add(key);
                }
            }
        }

        // 2. Paginación
        int enchantsPerPage = ENCHANT_SLOTS.length;
        int start = page * enchantsPerPage;
        int end = Math.min(start + enchantsPerPage, compatibleEnchants.size());

        if (hand != null && hand.getType() != Material.AIR) {
            ItemStack handCopy = hand.clone();
            inv.setItem(10, handCopy);
        }

        for (int i = start; i < end; i++) {
            String key = compatibleEnchants.get(i);
            ConfigurationSection itemSec = itemsSection.getConfigurationSection(key);
            if (itemSec == null) continue;

            String materialName = itemSec.getString("material", "STONE");
            Material material = Material.getMaterial(materialName.toUpperCase());
            if (material == null) material = Material.STONE;

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (itemSec.contains("name")) {
                String rawName = itemSec.getString("name").replace("&", "§");
                meta.setDisplayName(com.stp.utils.PlaceholderUtil.applyPlaceholders(player, rawName));
            }

            if (itemSec.contains("lore")) {
                List<String> lore = itemSec.getStringList("lore");
                List<String> finalLore = new ArrayList<>();
                for (String line : lore) {
                    String rawLore = line.replace("&", "§");
                    if (rawLore.equals("%pickaxe_lore%")) {
                        if (hand != null && hand.hasItemMeta() && hand.getItemMeta().hasLore()) {
                            finalLore.addAll(hand.getItemMeta().getLore());
                        }
                    } else {
                        for (String wrapped : wrapLoreLine(com.stp.utils.PlaceholderUtil.applyPlaceholders(player, rawLore), 40)) {
                            finalLore.add(wrapped);
                        }
                    }
                }
                meta.setLore(finalLore);
            }

            item.setItemMeta(meta);
            inv.setItem(ENCHANT_SLOTS[i - start], item);
        }

        // Botón de siguiente página si hay más
        if (end < compatibleEnchants.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta meta = nextPage.getItemMeta();
            meta.setDisplayName("§eSiguiente página");
            nextPage.setItemMeta(meta);
            inv.setItem(53, nextPage); // Slot 53: esquina inferior derecha
        }

        // Botón de página anterior si no es la primera
        if (page > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta meta = prevPage.getItemMeta();
            meta.setDisplayName("§ePágina anterior");
            prevPage.setItemMeta(meta);
            inv.setItem(45, prevPage); // Slot 45: esquina inferior izquierda
        }

        return inv;
    }

    public static List<String> wrapLoreLine(String line, int maxLength) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int colorIndex = 0;
        String lastColors = "";

        for (String word : line.split(" ")) {
            if (word.contains("§")) {
                colorIndex = word.lastIndexOf("§");
                if (colorIndex != -1 && word.length() > colorIndex + 1) {
                    lastColors = word.substring(colorIndex, colorIndex + 2);
                }
            }
            if (current.length() + word.length() + 1 > maxLength) {
                result.add(current.toString());
                current = new StringBuilder(lastColors + word);
            } else {
                if (current.length() > 0) current.append(" ");
                current.append(word);
            }
        }
        if (current.length() > 0) result.add(current.toString());
        return result;
    }
}