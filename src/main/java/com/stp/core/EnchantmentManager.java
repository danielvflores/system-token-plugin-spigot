package com.stp.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.enchants.CustomEnchant;
import com.stp.objects.Pickaxe;

public class EnchantmentManager {
    private final Pickaxe pickaxe = new Pickaxe();
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
        
        int lvl = pickaxe.getCustomEnchantmentLevel(item, enchantId);
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

    public java.math.BigDecimal getCurrentCost(Player player, String enchantId) {
        int currentLevel = getCurrentLevel(player, enchantId);
        org.bukkit.configuration.file.FileConfiguration config = com.stp.core.PrisonEnchantCustom.getInstance().getConfig();
        String model = config.getString("model", "LINEAL").toUpperCase();
        String path = "enchants." + enchantId + ".cost-per-level";
        int costPerLevel = config.getInt(path, 1000);

        switch (model) {
            case "LINEAL":
                return java.math.BigDecimal.valueOf(costPerLevel).multiply(java.math.BigDecimal.valueOf(currentLevel + 1));
            case "LOGARITHMIC":
                double logValue = Math.log(currentLevel + 2);
                return java.math.BigDecimal.valueOf(costPerLevel * logValue);
            case "EXPONENTIAL":
                double factor = config.getDouble("enchants." + enchantId + ".factor", 1.5);
                double expValue = costPerLevel * Math.pow(factor, currentLevel + 1);
                return java.math.BigDecimal.valueOf(expValue);
            case "PROGRESSIVE_ARITHMETIC":
                int increment = config.getInt("enchants." + enchantId + ".incremento", 500);
                int price = costPerLevel + (currentLevel) * increment;
                return java.math.BigDecimal.valueOf(price);
            default:
                return java.math.BigDecimal.valueOf(costPerLevel).multiply(java.math.BigDecimal.valueOf(currentLevel + 1));
        }
    }

    public String getCurrentCostFormatted(Player player, String enchantId) {
        java.math.BigDecimal cost = getCurrentCost(player, enchantId);
        return com.stp.utils.NumberUtils.formatWithSuffix(cost);
    }

    public boolean isEnchantmentRegistered(String enchantId) {
        return enchants.containsKey(enchantId.toLowerCase());
    }
    public Class<? extends CustomEnchant> getEnchantmentClass(String enchantId) {
        return enchants.get(enchantId.toLowerCase());
    }
    
}