package com.stp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.stp.core.SystemTokenEnchant;
import com.stp.utils.MessageUtils;

public class EnchantReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("stp.reload")) {
            sender.sendMessage(MessageUtils.getMessage("general.no-permission"));
            return true;
        }

        try {
            SystemTokenEnchant plugin = SystemTokenEnchant.getInstance();
            
            sender.sendMessage(MessageUtils.getMessage("message-prefix") + "§eIniciando recarga del plugin...");
            
            plugin.reloadConfig();
            sender.sendMessage(MessageUtils.getMessage("message-prefix") + "§aConfiguración recargada.");
            
            plugin.getEnchantmentManager().clearEnchants();
            sender.sendMessage(MessageUtils.getMessage("message-prefix") + "§aEncantamientos limpiados.");
            
            reregisterEnchantments(plugin);
            sender.sendMessage(MessageUtils.getMessage("message-prefix") + "§aEncantamientos re-registrados.");
            
            new com.stp.core.EnchantmentLoader(plugin).loadEnchantments();
            sender.sendMessage(MessageUtils.getMessage("message-prefix") + "§aEncantamientos externos cargados.");
            
            sender.sendMessage(MessageUtils.getMessage("general.enchants-reloaded"));
            
            plugin.getLogger().info("Plugin recargado por: " + sender.getName());
            
        } catch (Exception e) {
            sender.sendMessage(MessageUtils.getMessage("message-prefix") + "§cError durante la recarga: " + e.getMessage());
            SystemTokenEnchant.getInstance().getLogger().severe("Error durante reload: " + e.getMessage());
            return true;
        }
        
        return true;
    }
    
    @SuppressWarnings("unchecked")
    private void reregisterEnchantments(SystemTokenEnchant plugin) {
        Class<? extends com.stp.enchants.CustomEnchant>[] enchantClasses;
        enchantClasses = new Class[] {
            com.stp.enchants.impl.Speed.class,
            com.stp.enchants.impl.Explosive.class,
            com.stp.enchants.impl.Efficiency.class,
            com.stp.enchants.impl.Fortune.class,
            com.stp.enchants.impl.Fly.class,
            com.stp.enchants.impl.Nuke.class,
            com.stp.enchants.impl.GiveToken.class,
            com.stp.enchants.impl.GiveMoney.class,
            com.stp.enchants.impl.Strength.class
        };

        for (Class<? extends com.stp.enchants.CustomEnchant> enchantClass : enchantClasses) {
            try {
                com.stp.enchants.CustomEnchant enchant = enchantClass.getConstructor(int.class).newInstance(0);
                plugin.getEnchantmentManager().registerEnchantment(enchant.getId(), enchantClass);
                plugin.getLogger().info("Encantamiento re-registrado: " + enchant.getId());
            } catch (Exception e) {
                plugin.getLogger().warning("No se pudo re-registrar encantamiento: " + enchantClass.getSimpleName());
            }
        }
    }
}