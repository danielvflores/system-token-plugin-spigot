package com.stp.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.stp.enchants.CustomEnchant;

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
}