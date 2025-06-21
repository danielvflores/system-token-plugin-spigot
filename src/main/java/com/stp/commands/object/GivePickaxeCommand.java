package com.stp.commands.object;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.stp.objects.Pickaxe;

public class GivePickaxeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede usarse en el juego.");
            return true;
        }

        Player player = (Player) sender;
        player.getInventory().addItem(Pickaxe.create(player));
        player.sendMessage("§aHas recibido el §bPico Supremo§a.");
        return true;
    }
}
