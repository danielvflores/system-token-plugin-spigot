package com.stp.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.stp.core.SystemTokenEnchant;
import com.stp.db.PickaxeStorage;
import com.stp.enchants.CustomEnchant;
import com.stp.enchants.impl.Explosive;
import com.stp.enchants.impl.GiveMoney;
import com.stp.enchants.impl.GiveToken;
import com.stp.enchants.impl.Nuke;
import com.stp.enchants.impl.Vampire;
import com.stp.objects.Pickaxe;
import com.stp.utils.MessageUtils;

public class PickaxeListener implements Listener {

    private final Pickaxe pickaxe = new Pickaxe();
    private final Map<UUID, Long> dropCooldown = new HashMap<>();
    private final PickaxeStorage storage = new PickaxeStorage(SystemTokenEnchant.getInstance().getDatabaseManager());

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack dropped = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();

        if (!pickaxe.isCustomItem(dropped)) return;

        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();

        if (!dropCooldown.containsKey(uuid) || now - dropCooldown.get(uuid) > 2000) {
            event.setCancelled(true);
            dropCooldown.put(uuid, now);
            player.sendMessage(MessageUtils.getMessage("pickaxe.drop-item"));
        } else {
            dropCooldown.remove(uuid);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<ItemStack> toRemove = new ArrayList<>();
        for (ItemStack item : event.getDrops()) {
            if (pickaxe.isCustomItem(item)) {
                storage.savePickaxe(player.getUniqueId(), item.clone());
                toRemove.add(item);
            }
        }
        event.getDrops().removeAll(toRemove);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        ItemStack pickaxeItem = storage.loadPickaxe(player.getUniqueId());
        if (pickaxeItem != null) {
            player.getInventory().addItem(pickaxeItem);
            storage.deletePickaxe(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if (!pickaxe.isCustomItem(item)) return;

        Block block = event.getBlock();
        Material blockType = block.getType();

        List<Material> allowedBlocks = SystemTokenEnchant.getInstance().getAllowedBlocks();
        if (!allowedBlocks.contains(blockType)) {
            event.setCancelled(true);
            player.sendMessage(MessageUtils.getMessage("pickaxe.invalid-block"));
            return;
        }

        for (String enchantId : SystemTokenEnchant.getInstance().getEnchantmentManager().getRegisteredEnchants()) {
            int level = pickaxe.getCustomEnchantmentLevel(item, enchantId);
            if (level <= 0) continue;

            CustomEnchant enchant = SystemTokenEnchant.getInstance().getEnchantmentManager().createEnchantment(enchantId, level);
            if (enchant == null) continue;

            if (enchant instanceof Explosive) {
                if (Explosive.isExploding.get()) return;
                ((Explosive) enchant).handleBlockBreak(event, player, level);
            }

            if (enchant instanceof Nuke) {
                if (Nuke.isNuking.get()) return;
                ((Nuke) enchant).handleBlockBreak(event, player, level);
            }

            if (enchant instanceof GiveToken) {
                ((GiveToken) enchant).handleBlockBreak(player, item);
            }
            if (enchant instanceof GiveMoney) {
                ((GiveMoney) enchant).handleBlockBreak(player, item);
            }
        }
    }

    private final Set<UUID> noFallDamage = new HashSet<>();
    private final Set<UUID> pluginFly = new HashSet<>();

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());

        if (pickaxe.isCustomItem(newItem)) {
            int flyLevel = pickaxe.getCustomEnchantmentLevel(newItem, "fly");
            if (flyLevel > 0) {
                CustomEnchant flyEnchant = SystemTokenEnchant.getInstance().getEnchantmentManager().createEnchantment("fly", flyLevel);
                if (flyEnchant != null) {
                    flyEnchant.onEnable(player, flyLevel);
                    pluginFly.add(player.getUniqueId());
                }
                return;
            }
        }

        if (pluginFly.remove(player.getUniqueId())) {
            CustomEnchant flyEnchant = SystemTokenEnchant.getInstance().getEnchantmentManager().createEnchantment("fly", 1);
            if (flyEnchant != null) {
                flyEnchant.onDisable(player);
                noFallDamage.add(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Player player = (Player) event.getEntity();
            if (noFallDamage.remove(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Solo procesar si el atacante es un jugador
        if (!(event.getDamager() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        ItemStack weapon = attacker.getItemInHand();

        // Verificar que el arma sea un item personalizado
        if (!pickaxe.isCustomItem(weapon)) return;

        // Verificar si tiene el encantamiento vampire
        int vampireLevel = pickaxe.getCustomEnchantmentLevel(weapon, "vampire");
        if (vampireLevel <= 0) return;

        // Crear instancia del encantamiento vampire
        CustomEnchant vampireEnchant = SystemTokenEnchant.getInstance()
                .getEnchantmentManager().createEnchantment("vampire", vampireLevel);
        if (vampireEnchant == null || !(vampireEnchant instanceof Vampire)) return;

        // Obtener configuración del vampirismo
        double extraDamagePerLevel = SystemTokenEnchant.getInstance().getConfig()
                .getDouble("enchants.vampire.extra-damage-per-level", 2.0);
        double healPercentage = SystemTokenEnchant.getInstance().getConfig()
                .getDouble("enchants.vampire.heal-percentage", 50.0);

        // Calcular daño extra y curación
        double extraDamage = extraDamagePerLevel * vampireLevel;
        double healAmount = (extraDamage * healPercentage) / 100.0;

        // Aplicar daño extra
        event.setDamage(event.getDamage() + extraDamage);

        // Curar al atacante
        double currentHealth = attacker.getHealth();
        double maxHealth = attacker.getMaxHealth();
        double newHealth = Math.min(currentHealth + healAmount, maxHealth);
        
        attacker.setHealth(newHealth);

        // Enviar mensaje al atacante
        String prefix = SystemTokenEnchant.getInstance().getConfig().getString("message-prefix", "");
        String message = SystemTokenEnchant.getInstance().getConfig()
                .getString("messages.vampire.healed", "¡Vampirismo activado! Has curado &e%health% &7de salud.")
                .replace("%health%", String.format("%.1f", healAmount));
        
        attacker.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', prefix + message));
    }
}