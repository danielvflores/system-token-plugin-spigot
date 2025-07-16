package com.stp.commands.object;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.stp.core.SystemTokenEnchant;
import com.stp.enchants.CustomEnchant;
import com.stp.objects.Pickaxe;
import com.stp.utils.MessageUtils;

public class GivePickaxeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.getMessage("pickaxe.only-player"));
            return true;
        }

        Player player = (Player) sender;
        Pickaxe pickaxe = new Pickaxe();
        ItemStack pickaxeItem = pickaxe.create(player);

        org.bukkit.configuration.ConfigurationSection enchantSection = 
            SystemTokenEnchant.getInstance().getConfig().getConfigurationSection("pickaxe.give-command-enchants");
        
        if (enchantSection != null) {
            for (String enchantId : enchantSection.getKeys(false)) {
                int level = enchantSection.getInt(enchantId, 1);

                if (SystemTokenEnchant.getInstance().getEnchantmentManager().isEnchantmentRegistered(enchantId)) {
                    CustomEnchant enchant = SystemTokenEnchant.getInstance()
                        .getEnchantmentManager().createEnchantment(enchantId, level);
                    
                    if (enchant != null) {
                        pickaxeItem = pickaxe.addCustomEnchantment(pickaxeItem, enchant, player);
                    }
                }
            }
        }
        
        player.getInventory().addItem(pickaxeItem);
        player.sendMessage(MessageUtils.getMessage("pickaxe.received"));
        return true;
    }
}