package com.stp.commands.object;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        player.getInventory().addItem(pickaxe.create(player));
        player.sendMessage(MessageUtils.getMessage("pickaxe.received"));
        return true;
    }
}