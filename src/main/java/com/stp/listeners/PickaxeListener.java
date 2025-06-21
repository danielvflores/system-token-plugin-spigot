package com.stp.listeners;

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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.stp.core.PrisonEnchantCustom;
import com.stp.db.PickaxeStorage;
import com.stp.enchants.CustomEnchant;
import com.stp.enchants.impl.Explosive;
import com.stp.enchants.impl.Fortune;
import com.stp.objects.Pickaxe;

public class PickaxeListener implements Listener {

    private final Set<Material> fortuneBlocks = new HashSet<>();
    private final Map<UUID, Long> dropCooldown = new HashMap<>();
    private final PickaxeStorage storage = new PickaxeStorage(PrisonEnchantCustom.getInstance().getDatabaseManager());

    public PickaxeListener() {
        List<String> blockNames = PrisonEnchantCustom.getInstance().getConfig().getStringList("fortune-blocks");
        for (String name : blockNames) {
            Material mat = Material.matchMaterial(name.toUpperCase());
            if (mat != null) fortuneBlocks.add(mat);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack dropped = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();

        if (!Pickaxe.isCustomPickaxe(dropped)) return;

        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();

        if (!dropCooldown.containsKey(uuid) || now - dropCooldown.get(uuid) > 2000) {
            event.setCancelled(true);
            dropCooldown.put(uuid, now);
            player.sendMessage("\u00a7cPresiona Q nuevamente en menos de 2 segundos para tirar el pico.");
        } else {
            dropCooldown.remove(uuid);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (ItemStack item : event.getDrops()) {
            if (Pickaxe.isCustomPickaxe(item)) {
                storage.savePickaxe(player.getUniqueId(), item.clone());
                break;
            }
        }
        event.getDrops().removeIf(Pickaxe::isCustomPickaxe);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        ItemStack pickaxe = storage.loadPickaxe(player.getUniqueId());
        if (pickaxe != null) {
            player.getInventory().addItem(pickaxe);
            storage.deletePickaxe(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        // if (Explosive.isExploding.get()) return; activar si no funciona el move to instanceof

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if (!Pickaxe.isCustomPickaxe(item)) return;

        Block block = event.getBlock();
        Material blockType = block.getType();

        List<Material> allowedBlocks = PrisonEnchantCustom.getInstance().getAllowedBlocks();
        if (!allowedBlocks.contains(blockType)) {
            event.setCancelled(true);
            return;
        }

        for (String enchantId : PrisonEnchantCustom.getInstance().getEnchantmentManager().getRegisteredEnchants()) {
            int level = Pickaxe.getCustomEnchantmentLevel(item, enchantId);
            if (level <= 0) continue;

            CustomEnchant enchant = PrisonEnchantCustom.getInstance().getEnchantmentManager().createEnchantment(enchantId, level);
            if (enchant == null) continue;

            if (enchant instanceof Explosive) {
                if (Explosive.isExploding.get()) return;
                ((Explosive) enchant).handleBlockBreak(event, player, level);
            }

            if (enchant instanceof Fortune && fortuneBlocks.contains(blockType)) {
                ((Fortune) enchant).applyEffect(player, level);
            }
        }
    }

    private final Set<UUID> noFallDamage = new HashSet<>();

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        
        if (Pickaxe.isCustomPickaxe(newItem)) {
            int flyLevel = Pickaxe.getCustomEnchantmentLevel(newItem, "fly");
            if (flyLevel > 0) {
                CustomEnchant flyEnchant = PrisonEnchantCustom.getInstance().getEnchantmentManager().createEnchantment("fly", flyLevel);
                if (flyEnchant != null) {
                    flyEnchant.onEnable(player, flyLevel);
                }
                return;
            }
        }

        CustomEnchant flyEnchant = PrisonEnchantCustom.getInstance().getEnchantmentManager().createEnchantment("fly", 1);
        if (flyEnchant != null) {
            flyEnchant.onDisable(player);
            noFallDamage.add(player.getUniqueId());
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
}
