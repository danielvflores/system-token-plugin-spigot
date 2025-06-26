package com.stp.objects;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;
import com.stp.utils.PlaceholderUtil;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Pickaxe implements CustomItem {
    public static final String PICKAXE_KEY = "custom_pickaxe";

    @Override
    public ItemStack create(Player player) {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();

        String rawName = PrisonEnchantCustom.getInstance().getConfig()
                .getString("pickaxe.display-name", "&3&lCustom Pickaxe");

        String displayName = PlaceholderUtil.applyPlaceholders(player, rawName, new HashMap<>());
        meta.setDisplayName(color(displayName));

        List<String> loreConfig = PrisonEnchantCustom.getInstance().getConfig().getStringList("pickaxe.lore");
        List<String> lore = new ArrayList<>();
        for (String line : loreConfig) {
            if (!line.contains("{}")) {
                String processed = PlaceholderUtil.applyPlaceholders(player, line, new HashMap<>());
                lore.add(color(processed));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        tag.setBoolean(PICKAXE_KEY, true);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public boolean isCustomItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem != null && nmsItem.hasTag() && nmsItem.getTag().getBoolean(PICKAXE_KEY);
    }

    @Override
    public ItemStack addCustomEnchantment(ItemStack item, CustomEnchant enchant, Player player) {
        if (item == null || item.getType() == Material.AIR) return item;

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        tag.setBoolean(PICKAXE_KEY, true);
        tag.setInt(enchant.getId().toLowerCase(), enchant.getLevel());
        nmsItem.setTag(tag);

        ItemStack bukkitItem = CraftItemStack.asBukkitCopy(nmsItem);
        refreshLore(bukkitItem, player);
        return bukkitItem;
    }

    @Override
    public int getCustomEnchantmentLevel(ItemStack item, String enchantId) {
        if (item == null || item.getType() == Material.AIR) return 0;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem == null || !nmsItem.hasTag()) return 0;
        NBTTagCompound tag = nmsItem.getTag();
        return tag.hasKey(enchantId.toLowerCase()) ? tag.getInt(enchantId.toLowerCase()) : 0;
    }

    @Override
    public CustomEnchant getCustomEnchantment(ItemStack item, String enchantId) {
        if (!isCustomItem(item)) return null;
        int level = getCustomEnchantmentLevel(item, enchantId);
        return level > 0 ? PrisonEnchantCustom.getInstance()
                .getEnchantmentManager()
                .createEnchantment(enchantId, level) : null;
    }

    @Override
    public ItemStack removeCustomEnchantment(ItemStack item, String enchantId, Player player) {
        if (item == null || item.getType() == Material.AIR) return item;

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem == null || !nmsItem.hasTag()) return item;

        NBTTagCompound tag = nmsItem.getTag();
        tag.remove(enchantId.toLowerCase());
        nmsItem.setTag(tag);

        ItemStack bukkitItem = CraftItemStack.asBukkitCopy(nmsItem);
        refreshLore(bukkitItem, player);
        return bukkitItem;
    }

    @Override
    public void refreshLore(ItemStack item, Player player) {
        if (item == null || item.getType() == Material.AIR) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        List<String> baseLore = PrisonEnchantCustom.getInstance().getConfig().getStringList("pickaxe.lore");
        List<String> newLore = new ArrayList<>();
        List<String> enchantLore = new ArrayList<>();

        for (String enchantId : PrisonEnchantCustom.getInstance()
                .getEnchantmentManager().getRegisteredEnchants()) {

            CustomEnchant enchant = getCustomEnchantment(item, enchantId);
            if (enchant != null && getCustomEnchantmentLevel(item, enchantId) > 0) {
                String display = PrisonEnchantCustom.getInstance().getConfig()
                        .getString("enchants." + enchantId + ".display", enchant.getDisplayName());
                enchantLore.add(color(display) + " " + MessageFormat.format("({0})", toRoman(enchant.getLevel())));
            }
        }

        for (String line : baseLore) {
            if (line.contains("{}")) {
                if (enchantLore.isEmpty()) continue;
                for (String enchantLine : enchantLore) {
                    String processed = line.replace("{}", enchantLine);
                    newLore.add(color(PlaceholderUtil.applyPlaceholders(player, processed, new HashMap<>())));
                }
            } else {
                String processed = PlaceholderUtil.applyPlaceholders(player, line, new HashMap<>());
                newLore.add(color(processed));
            }
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
    }

    private static String toRoman(int number) {
        switch (number) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            default: return Integer.toString(number);
        }
    }

    private static String color(String text) {
        return text.replace("&", "§");
    }
}