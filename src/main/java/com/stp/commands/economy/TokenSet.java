package com.stp.commands.economy;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.stp.commands.SubCommand;
import com.stp.economy.TokenManager;
import com.stp.utils.NumberUtils;

public class TokenSet implements SubCommand {

    private final TokenManager tokenManager;

    public TokenSet(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (!sender.hasPermission("token.set")) {
            sender.sendMessage("§cNo tienes permiso para usar este comando.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§cUso: /token set <jugador> <cantidad>");
            return true;
        }

        String targetName = args[1];
        Player target = sender.getServer().getPlayerExact(targetName);

        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return true;
        }

        BigDecimal amount;
        try {
            amount = NumberUtils.parseAmountWithSuffix(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cLa cantidad debe ser un número positivo válido (ej: 1500, 1.5K, 2M, 3B, 1.5Q).");
            return true;
        }

        tokenManager.setTokens(target.getUniqueId(), amount);
        sender.sendMessage("§aSe establecieron §e" + amount.toPlainString() + " §atokens a " + target.getName() + ".");
        target.sendMessage("§aTus tokens ahora son: §e" + amount.toPlainString());
        return true;
    }
}
