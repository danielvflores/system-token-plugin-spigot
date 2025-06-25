package com.stp.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;
import com.stp.objects.Pickaxe;
import com.stp.utils.MessageUtils;

public class EnchantGUIListener implements Listener {

    private final Map<Integer, String> slotToEnchantId = new HashMap<>();
    private String guiTitle;

    public EnchantGUIListener() {

        ConfigurationSection guiSection = PrisonEnchantCustom.getInstance().getConfig().getConfigurationSection("enchant-gui.items");
        if (guiSection != null) {
            for (String key : guiSection.getKeys(false)) {
                ConfigurationSection itemSec = guiSection.getConfigurationSection(key);
                if (itemSec == null) continue;
                int slot = itemSec.getInt("slot", -1);

                if (slot < 0) continue;

                if (PrisonEnchantCustom.getInstance().getConfig().isConfigurationSection("enchants." + key)) {
                    slotToEnchantId.put(slot, key);
                }
            }
        }

        ConfigurationSection guiMain = PrisonEnchantCustom.getInstance().getConfig().getConfigurationSection("enchant-gui");
        if (guiMain != null) {
            guiTitle = guiMain.getString("title", "Enchant Menu").replace('&', '§');
        } else {
            guiTitle = "Enchant Menu";
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();

        if (!inv.getTitle().equals(guiTitle)) return;

        event.setCancelled(true);
        int slot = event.getRawSlot();

        if (!slotToEnchantId.containsKey(slot)) return;

        String enchantId = slotToEnchantId.get(slot);
        ItemStack item = player.getInventory().getItemInHand();
        
        if (item == null || item.getType() == Material.AIR || !Pickaxe.isCustomPickaxe(item)) {
            player.sendMessage(MessageUtils.getMessage("enchant.no-pickaxe"));
            return;
        }

        int currentLevel = Pickaxe.getCustomEnchantmentLevel(item, enchantId);
        CustomEnchant enchant = PrisonEnchantCustom.getInstance().getEnchantmentManager().createEnchantment(enchantId, currentLevel + 1);
        
        if (enchant == null) {
            player.sendMessage(MessageUtils.getMessage("enchant.unknown").replace("%enchant%", enchantId));
            return;
        }

        if (currentLevel + 1 > enchant.getMaxLevel()) {
            player.sendMessage(MessageUtils.getMessage("enchant.invalid-range")
                    .replace("%min%", "0")
                    .replace("%max%", String.valueOf(enchant.getMaxLevel())));
            return;
        }

        java.math.BigDecimal cost = PrisonEnchantCustom.getInstance().getEnchantmentManager().getCurrentCost(player, enchantId);
        boolean success = PrisonEnchantCustom.getInstance().getTokenManager().removeTokens(player.getUniqueId(), cost);
        if (!success) {
            player.sendMessage(MessageUtils.getMessage("token.insufficient-tokens"));
            return;
        }

        ItemStack newItem = Pickaxe.addCustomEnchantment(item, enchant, player);
        player.getInventory().setItemInHand(newItem);
        enchant.onEnable(player, currentLevel + 1);
        String msg = MessageUtils.getMessage("enchant.applied")
                .replace("%enchant%", getDisplayNamePlain(enchant.getDisplayName()))
                .replace("%player%", player.getName())
                .replace("%level%", String.valueOf(currentLevel + 1));
        player.sendMessage(msg);

        Bukkit.getScheduler().runTaskLater(PrisonEnchantCustom.getInstance(), () -> {
            player.openInventory(com.stp.gui.EnchantGUI.createEnchantGUI(player));
        }, 2L);
    }

    private String getDisplayNamePlain(String displayName) {
        String colored = displayName.replace("&", "§");
        return colored.replaceAll("§[0-9a-fk-or]", "");
    }
}
