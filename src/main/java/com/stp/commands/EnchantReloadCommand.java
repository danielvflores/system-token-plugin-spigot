package com.stp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.stp.core.PrisonEnchantCustom;
import com.stp.utils.MessageUtils;

public class EnchantReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PrisonEnchantCustom plugin = PrisonEnchantCustom.getInstance();
        plugin.reloadConfig(); // Recarga el config.yml
        plugin.getEnchantmentManager().clearEnchants();
        new com.stp.core.EnchantmentLoader(plugin).loadEnchantments();
        sender.sendMessage(MessageUtils.getMessage("general.enchants-reloaded"));
        return true;
    }
}