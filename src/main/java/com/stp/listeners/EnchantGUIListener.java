package com.stp.listeners;

import java.util.ArrayList;
import java.util.List;

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
import com.stp.gui.EnchantGUI;
import com.stp.objects.Pickaxe;
import com.stp.utils.MessageUtils;

public class EnchantGUIListener implements Listener {
    private static final int[] ENCHANT_SLOTS = {12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34};

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();

        String guiTitle = PrisonEnchantCustom.getInstance().getConfig().getString("enchant-gui.title", "Enchant Menu").replace('&', '§');
        if (!inv.getTitle().equals(guiTitle)) return;

        event.setCancelled(true);
        int slot = event.getRawSlot();

        // Detectar página actual por el lore del botón o por un sistema propio (aquí ejemplo simple)
        int page = 0;
        // Si quieres guardar la página en el nombre del inventario, puedes hacerlo así:
        // String title = inv.getTitle();
        // if (title.contains("Página ")) page = Integer.parseInt(title.split("Página ")[1]) - 1;

        // Obtener lista de encantamientos compatibles para la página actual
        List<String> compatibleEnchants = getCompatibleEnchants(player);

        // Botón siguiente página
        if (slot == 53 && (page + 1) * ENCHANT_SLOTS.length < compatibleEnchants.size()) {
            player.openInventory(EnchantGUI.createEnchantGUI(player, page + 1));
            return;
        }
        // Botón página anterior
        if (slot == 45 && page > 0) {
            player.openInventory(EnchantGUI.createEnchantGUI(player, page - 1));
            return;
        }

        // Click en un slot de encantamiento
        int enchantsPerPage = ENCHANT_SLOTS.length;
        int start = page * enchantsPerPage;
        int end = Math.min(start + enchantsPerPage, compatibleEnchants.size());

        for (int i = start; i < end; i++) {
            if (slot == ENCHANT_SLOTS[i - start]) {
                String enchantId = compatibleEnchants.get(i);

                ItemStack item = player.getInventory().getItemInHand();
                if (item == null || item.getType() == Material.AIR) {
                    player.sendMessage(MessageUtils.getMessage("enchant.no-pickaxe"));
                    return;
                }

                Pickaxe pickaxe = new Pickaxe();
                int currentLevel = pickaxe.getCustomEnchantmentLevel(item, enchantId);
                CustomEnchant enchant = PrisonEnchantCustom.getInstance().getEnchantmentManager().createEnchantment(enchantId, currentLevel + 1);

                if (enchant == null) {
                    player.sendMessage(MessageUtils.getMessage("enchant.unknown").replace("%enchant%", enchantId));
                    return;
                }

                if (!enchant.canEnchantItem(item)) {
                    player.sendMessage(MessageUtils.getMessage("enchant.not-custom-pickaxe"));
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

                ItemStack newItem = pickaxe.addCustomEnchantment(item, enchant, player);
                player.getInventory().setItemInHand(newItem);
                enchant.onEnable(player, currentLevel + 1);
                String msg = MessageUtils.getMessage("enchant.applied")
                        .replace("%enchant%", getDisplayNamePlain(enchant.getDisplayName()))
                        .replace("%player%", player.getName())
                        .replace("%level%", String.valueOf(currentLevel + 1));
                player.sendMessage(msg);

                Bukkit.getScheduler().runTaskLater(PrisonEnchantCustom.getInstance(), () -> {
                    player.openInventory(EnchantGUI.createEnchantGUI(player, page));
                }, 2L);
                return;
            }
        }
    }

    // Devuelve la lista de encantamientos compatibles para el ítem en mano del jugador
    private List<String> getCompatibleEnchants(Player player) {
        List<String> compatibleEnchants = new ArrayList<>();
        ConfigurationSection itemsSection = PrisonEnchantCustom.getInstance().getConfig().getConfigurationSection("enchant-gui.items");
        ItemStack hand = player.getInventory().getItemInHand();

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                if (PrisonEnchantCustom.getInstance().getConfig().isConfigurationSection("enchants." + key)) {
                    List<String> allowedTypes = PrisonEnchantCustom.getInstance().getConfig()
                            .getStringList("enchants." + key + ".enchants-item-avaible");
                    boolean strict = PrisonEnchantCustom.getInstance().getConfig()
                            .getBoolean("enchants." + key + ".enchant-strict", false);

                    String typeName = hand != null ? hand.getType().name() : "";
                    boolean typeAllowed = allowedTypes.stream().anyMatch(typeName::endsWith);

                    boolean show = false;
                    if (typeAllowed) {
                        if (strict) {
                            String requiredName = PrisonEnchantCustom.getInstance().getConfig()
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
        return compatibleEnchants;
    }

    private String getDisplayNamePlain(String displayName) {
        String colored = displayName.replace("&", "§");
        return colored.replaceAll("§[0-9a-fk-or]", "");
    }
}