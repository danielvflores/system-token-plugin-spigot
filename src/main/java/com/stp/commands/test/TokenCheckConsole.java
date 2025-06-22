package com.stp.commands.test;

import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.stp.commands.SubCommand;
import com.stp.economy.TokenManager;

public class TokenCheckConsole implements SubCommand {

    private final TokenManager tokenManager;

    public TokenCheckConsole(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Solo consola puede usarlo
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("§cEste comando solo puede usarse desde la consola.");
            return true;
        }

        if (args.length < 2) { // ojo, args[0] será "tokencheck", args[1] el nombre
            System.out.println("Uso: /token tokencheck <jugador>");
            return true;
        }

        String playerName = args[1];
        Player player = Bukkit.getPlayerExact(playerName);

        if (player == null) {
            System.out.println("Jugador no encontrado o no está conectado.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        BigDecimal tokens = tokenManager.getTokens(uuid);
        System.out.println("El jugador " + player.getName() + " tiene exactamente " + tokens.toPlainString() + " tokens.");
        return true;
    }
}
