package com.stp.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.enchants.CustomEnchant;
import com.stp.objects.Pickaxe;

public class EnchantmentManager {
    private final Map<String, Class<? extends CustomEnchant>> enchants = new HashMap<>();

    public void registerEnchantment(String id, Class<? extends CustomEnchant> enchantClass) {
        if (id == null || enchantClass == null) {
            throw new IllegalArgumentException("ID y clase de encantamiento no pueden ser nulos");
        }
        enchants.put(id.toLowerCase(), enchantClass);
    }

    public CustomEnchant createEnchantment(String id, int level) {
        try {
            Class<? extends CustomEnchant> clazz = enchants.get(id.toLowerCase());
            if (clazz != null) {
                return clazz.getConstructor(int.class).newInstance(level);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<String> getRegisteredEnchants() {
        return enchants.keySet();
    }

    public void clearEnchants() {
        enchants.clear();
    }

    public int getCurrentLevel(Player player, String enchantId) {
        ItemStack item = player.getInventory().getItemInHand();
        int lvl = Pickaxe.getCustomEnchantmentLevel(item, enchantId);
        return lvl;
    }

    public int getMaxLevel(String enchantId) {
        CustomEnchant enchant = createEnchantment(enchantId, 1);
        if (enchant != null) {
            return enchant.getMaxLevel();
        }
        return 0;
    }

    public String getEnchantmentName(String enchantId) {
        CustomEnchant enchant = createEnchantment(enchantId, 1);
        String name;
        if (enchant != null) {
            name = enchant.getDisplayName();
        } else {
            name = enchantId;
        }
        if (name.length() > 1) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            return name.toUpperCase();
        }
    }

    public boolean isEnchantmentRegistered(String enchantId) {
        return enchants.containsKey(enchantId.toLowerCase());
    }
    public Class<? extends CustomEnchant> getEnchantmentClass(String enchantId) {
        return enchants.get(enchantId.toLowerCase());
    }
    
}