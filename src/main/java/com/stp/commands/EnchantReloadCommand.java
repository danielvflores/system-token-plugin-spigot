package com.stp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.stp.core.PrisonEnchantCustom;

public class EnchantReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PrisonEnchantCustom.getInstance().getEnchantmentManager().clearEnchants();
        new com.stp.core.EnchantmentLoader(PrisonEnchantCustom.getInstance()).loadEnchantments();
        sender.sendMessage("§a¡Encantamientos recargados!");
        return true;
    }
}