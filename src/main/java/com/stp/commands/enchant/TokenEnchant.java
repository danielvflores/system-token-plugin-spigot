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

public class TokenEnchant implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage("§cUso: /token enchant <jugador> <encantamiento> <nivel>");
            return true;
        }

        String playerName = args[1];
        String enchantId = args[2].toLowerCase();
        String levelStr = args[3];

        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage("§cEl jugador no está conectado.");
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(levelStr);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cEl nivel debe ser un número válido.");
            return true;
        }

        ItemStack item = target.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            sender.sendMessage("§cEl jugador debe tener un pico en la mano.");
            return true;
        }

        if (!Pickaxe.isCustomPickaxe(item)) {
            sender.sendMessage("§cEl pico no es un Pico Supremo.");
            return true;
        }

        CustomEnchant enchant = PrisonEnchantCustom.getInstance()
            .getEnchantmentManager()
            .createEnchantment(enchantId, level);

        if (enchant == null) {
            sender.sendMessage("§cEncantamiento desconocido: " + enchantId);
            return true;
        }

        if (level < 0 || level > enchant.getMaxLevel()) {
            sender.sendMessage("§cNivel inválido. Debe estar entre 0 y " + enchant.getMaxLevel());
            return true;
        }

        if (level == 0) {
            ItemStack newItem = Pickaxe.removeCustomEnchantment(item, enchantId, target);
            target.getInventory().setItemInHand(newItem);
            enchant.onDisable(target);
            sender.sendMessage("§aEncantamiento " + enchant.getDisplayName() + " removido del jugador " + playerName);
        } else {
            ItemStack newItem = Pickaxe.addCustomEnchantment(item, enchant, target);
            target.getInventory().setItemInHand(newItem);
            enchant.onEnable(target, level);
            sender.sendMessage("§aEncantamiento " + enchant.getDisplayName() + " aplicado al jugador " + playerName + " con nivel " + level);
        }

        return true;
    }
}