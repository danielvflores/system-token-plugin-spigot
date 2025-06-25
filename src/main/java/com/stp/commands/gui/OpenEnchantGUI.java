package com.stp.commands.gui;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.stp.gui.EnchantGUI;

public class OpenEnchantGUI implements CommandExecutor {

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("stp.openenchantgui")) {
            player.sendMessage("You do not have permission to open the enchant GUI.");
            return true;
        }
        player.openInventory(EnchantGUI.createEnchantGUI(player));
        return true;
    }
}
