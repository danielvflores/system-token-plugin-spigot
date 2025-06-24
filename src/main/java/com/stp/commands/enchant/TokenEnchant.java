package com.stp.commands.enchant;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.commands.SubCommand;
import com.stp.core.PrisonEnchantCustom;
import com.stp.enchants.CustomEnchant;
import com.stp.objects.Pickaxe;
import com.stp.utils.MessageUtils;

public class TokenEnchant implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(MessageUtils.getMessage("enchant.usage"));
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "set":
                if (!managePermission(sender, "stp.enchant.set")) return true;
                return handleSet(sender, args);
            case "nextlevel":
                if (!managePermission(sender, "stp.enchant.nextlevel")) return true;
                return handleNextLevel(sender, args);
            case "downlevel":
                if (!managePermission(sender, "stp.enchant.downlevel")) return true;
                return handleDownLevel(sender, args);
            default:
                sender.sendMessage(MessageUtils.getMessage("enchant.usage"));
                return true;
        }
    }

    private boolean handleSet(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(MessageUtils.getMessage("enchant.usage"));
            return true;
        }
        String playerName = args[1];
        String enchantId = args[2].toLowerCase();
        String levelStr = args[3];

        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage(MessageUtils.getMessage("enchant.player-not-found"));
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(levelStr);
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtils.getMessage("enchant.invalid-level"));
            return true;
        }

        ItemStack item = target.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            sender.sendMessage(MessageUtils.getMessage("enchant.no-pickaxe"));
            return true;
        }

        if (!Pickaxe.isCustomPickaxe(item)) {
            sender.sendMessage(MessageUtils.getMessage("enchant.not-custom-pickaxe"));
            return true;
        }

        CustomEnchant enchant = PrisonEnchantCustom.getInstance()
            .getEnchantmentManager()
            .createEnchantment(enchantId, level);

        if (enchant == null) {
            sender.sendMessage(MessageUtils.getMessage("enchant.unknown").replace("%enchant%", enchantId));
            return true;
        }

        if (level < 0 || level > enchant.getMaxLevel()) {
            sender.sendMessage(MessageUtils.getMessage("enchant.invalid-range")
                .replace("%min%", "0")
                .replace("%max%", String.valueOf(enchant.getMaxLevel())));
            return true;
        }

        String msg;
        if (level == 0) {
            ItemStack newItem = Pickaxe.removeCustomEnchantment(item, enchantId, target);
            target.getInventory().setItemInHand(newItem);
            enchant.onDisable(target);
            msg = MessageUtils.getMessage("enchant.removed")
                .replace("%enchant%", getDisplayNamePlain(enchant.getDisplayName()))
                .replace("%player%", playerName);
        } else {
            ItemStack newItem = Pickaxe.addCustomEnchantment(item, enchant, target);
            target.getInventory().setItemInHand(newItem);
            enchant.onEnable(target, level);
            msg = MessageUtils.getMessage("enchant.applied")
                .replace("%enchant%", getDisplayNamePlain(enchant.getDisplayName()))
                .replace("%player%", playerName)
                .replace("%level%", String.valueOf(level));
        }
        if (sender != target) target.sendMessage(msg);
        sender.sendMessage(msg);
        return true;
    }

    private boolean handleNextLevel(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(MessageUtils.getMessage("enchant.usage"));
            return true;
        }
        String playerName = args[1];
        String enchantId = args[2].toLowerCase();

        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (!playerSender.getName().equalsIgnoreCase(playerName)
                    && !playerSender.hasPermission("stp.enchant.nextlevel.other")) {
                sender.sendMessage(MessageUtils.getMessage("general.no-permission"));
                return true;
            }
        }

        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage(MessageUtils.getMessage("enchant.player-not-found"));
            return true;
        }

        ItemStack item = target.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            sender.sendMessage(MessageUtils.getMessage("enchant.no-pickaxe"));
            return true;
        }

        if (!Pickaxe.isCustomPickaxe(item)) {
            sender.sendMessage(MessageUtils.getMessage("enchant.not-custom-pickaxe"));
            return true;
        }

        int currentLevel = Pickaxe.getCustomEnchantmentLevel(item, enchantId);
        CustomEnchant enchant = PrisonEnchantCustom.getInstance()
            .getEnchantmentManager()
            .createEnchantment(enchantId, currentLevel + 1);

        if (enchant == null) {
            sender.sendMessage(MessageUtils.getMessage("enchant.unknown").replace("%enchant%", enchantId));
            return true;
        }

        if (currentLevel + 1 > enchant.getMaxLevel()) {
            sender.sendMessage(MessageUtils.getMessage("enchant.invalid-range")
                .replace("%min%", "0")
                .replace("%max%", String.valueOf(enchant.getMaxLevel())));
            return true;
        }

        int cost = PrisonEnchantCustom.getInstance().getConfig()
            .getInt("enchants." + enchantId + ".cost-per-level", 1000);

        boolean success = PrisonEnchantCustom.getInstance().getTokenManager()
            .removeTokens(target.getUniqueId(), java.math.BigDecimal.valueOf(cost));
        if (!success) {
            sender.sendMessage(MessageUtils.getMessage("token.insufficient-tokens"));
            return true;
        }

        ItemStack newItem = Pickaxe.addCustomEnchantment(item, enchant, target);
        target.getInventory().setItemInHand(newItem);
        enchant.onEnable(target, currentLevel + 1);
        String msg = MessageUtils.getMessage("enchant.applied")
            .replace("%enchant%", getDisplayNamePlain(enchant.getDisplayName()))
            .replace("%player%", playerName)
            .replace("%level%", String.valueOf(currentLevel + 1));
        if (sender != target) target.sendMessage(msg);
        sender.sendMessage(msg);
        return true;
    }

    private boolean handleDownLevel(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(MessageUtils.getMessage("enchant.usage"));
            return true;
        }
        String playerName = args[1];
        String enchantId = args[2].toLowerCase();

        if (sender instanceof Player) {
            Player playerSender = (Player) sender;
            if (!playerSender.getName().equalsIgnoreCase(playerName)
                    && !playerSender.hasPermission("stp.enchant.downlevel.other")) {
                sender.sendMessage(MessageUtils.getMessage("general.no-permission"));
                return true;
            }
        }

        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage(MessageUtils.getMessage("enchant.player-not-found"));
            return true;
        }

        ItemStack item = target.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            sender.sendMessage(MessageUtils.getMessage("enchant.no-pickaxe"));
            return true;
        }

        if (!Pickaxe.isCustomPickaxe(item)) {
            sender.sendMessage(MessageUtils.getMessage("enchant.not-custom-pickaxe"));
            return true;
        }

        int currentLevel = Pickaxe.getCustomEnchantmentLevel(item, enchantId);
        if (currentLevel <= 0) {
            sender.sendMessage(MessageUtils.getMessage("enchant.invalid-range")
                .replace("%min%", "0")
                .replace("%max%", "0"));
            return true;
        }

        CustomEnchant enchant = PrisonEnchantCustom.getInstance()
            .getEnchantmentManager()
            .createEnchantment(enchantId, currentLevel - 1);

        if (enchant == null) {
            sender.sendMessage(MessageUtils.getMessage("enchant.unknown").replace("%enchant%", enchantId));
            return true;
        }

        int cost = PrisonEnchantCustom.getInstance().getConfig()
            .getInt("enchants." + enchantId + ".cost-per-level", 1000);
        int refund = (int) Math.round(cost * 0.9);

        String msg;
        if (currentLevel - 1 == 0) {
            ItemStack newItem = Pickaxe.removeCustomEnchantment(item, enchantId, target);
            target.getInventory().setItemInHand(newItem);
            enchant.onDisable(target);
            PrisonEnchantCustom.getInstance().getTokenManager()
                .addTokens(target.getUniqueId(), java.math.BigDecimal.valueOf(refund));
            msg = MessageUtils.getMessage("enchant.removed")
                .replace("%enchant%", getDisplayNamePlain(enchant.getDisplayName()))
                .replace("%player%", playerName);
        } else {
            ItemStack newItem = Pickaxe.addCustomEnchantment(item, enchant, target);
            target.getInventory().setItemInHand(newItem);
            enchant.onEnable(target, currentLevel - 1);
            PrisonEnchantCustom.getInstance().getTokenManager()
                .addTokens(target.getUniqueId(), java.math.BigDecimal.valueOf(refund));
            msg = MessageUtils.getMessage("enchant.applied")
                .replace("%enchant%", getDisplayNamePlain(enchant.getDisplayName()))
                .replace("%player%", playerName)
                .replace("%level%", String.valueOf(currentLevel - 1));
        }
        if (sender != target) target.sendMessage(msg);
        sender.sendMessage(msg);
        return true;
    }

    private String getDisplayNamePlain(String displayName) {
        String colored = displayName.replace("&", "§");
        return colored.replaceAll("§[0-9a-fk-or]", "");
    }

    private boolean managePermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(MessageUtils.getMessage("general.no-permission"));
            return false;
        }
        return true;
    }
}