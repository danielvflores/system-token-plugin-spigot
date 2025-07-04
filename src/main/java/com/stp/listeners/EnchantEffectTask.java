package com.stp.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.stp.core.SystemTokenEnchant;
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

            for (String enchantId : SystemTokenEnchant.getInstance()
                    .getEnchantmentManager().getRegisteredEnchants()) {

                int level = pickaxe.getCustomEnchantmentLevel(item, enchantId);
                if (level > 0) {
                    if (!activeEnchantments.containsKey(player.getUniqueId())) {
                        activeEnchantments.put(player.getUniqueId(), new HashMap<String, CustomEnchant>());
                    }
                    Map<String, CustomEnchant> playerEnchants = activeEnchantments.get(player.getUniqueId());
                    
                    CustomEnchant enchant;
                    if (!playerEnchants.containsKey(enchantId)) {
                        enchant = SystemTokenEnchant.getInstance()
                                .getEnchantmentManager()
                                .createEnchantment(enchantId, level);
                        playerEnchants.put(enchantId, enchant);
                    } else {
                        enchant = playerEnchants.get(enchantId);
                    }

                    enchant.applyEffect(player, level);
                } else {
                    Map<String, CustomEnchant> playerEnchants = activeEnchantments.get(player.getUniqueId());
                    if (playerEnchants == null) {
                        playerEnchants = new HashMap<String, CustomEnchant>();
                    }
                    CustomEnchant oldEnchant = playerEnchants.get(enchantId);

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
            for (CustomEnchant enchant : enchants.values()) {
                enchant.onDisable(player);
            }
            activeEnchantments.remove(player.getUniqueId());
        }
    }
}