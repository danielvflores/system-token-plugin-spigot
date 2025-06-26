package com.stp.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;
import com.stp.objects.Pickaxe;

public class EnchantEffectTask extends BukkitRunnable {
    private final Map<UUID, Map<String, CustomEnchant>> activeEnchantments = new HashMap<>();
    private final Pickaxe pickaxe = new Pickaxe();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack item = player.getInventory().getItemInHand();

            if (!pickaxe.isCustomItem(item)) {
                clearAllEffects(player);
                continue;
            }

            for (String enchantId : PrisonEnchantCustom.getInstance()
                    .getEnchantmentManager().getRegisteredEnchants()) {

                int level = pickaxe.getCustomEnchantmentLevel(item, enchantId);
                if (level > 0) {
                    CustomEnchant enchant = activeEnchantments
                        .computeIfAbsent(player.getUniqueId(), k -> new HashMap<String, CustomEnchant>())
                        .computeIfAbsent(enchantId, k -> 
                            PrisonEnchantCustom.getInstance()
                                .getEnchantmentManager()
                                .createEnchantment(enchantId, level));

                    enchant.applyEffect(player, level);
                } else {
                    CustomEnchant oldEnchant = activeEnchantments
                        .getOrDefault(player.getUniqueId(), new HashMap<String, CustomEnchant>())
                        .get(enchantId);

                    if (oldEnchant != null) {
                        oldEnchant.onDisable(player);
                        activeEnchantments.get(player.getUniqueId()).remove(enchantId);
                    }
                }
            }
        }
    }

    private void clearAllEffects(Player player) {
        Map<String, CustomEnchant> enchants = activeEnchantments.get(player.getUniqueId());
        if (enchants != null) {
            enchants.values().forEach(enchant -> enchant.onDisable(player));
            activeEnchantments.remove(player.getUniqueId());
        }
    }
}