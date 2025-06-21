package com.stp.commands.economy;

import java.math.BigDecimal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.stp.commands.SubCommand;
import com.stp.economy.TokenManager;
import com.stp.utils.NumberUtils;

public class TokenBalance implements SubCommand {

    private final TokenManager tokenManager;

    public TokenBalance(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando solo puede usarlo un jugador.");
            return true;
        }
        Player player = (Player) sender;
        BigDecimal tokens = tokenManager.getTokens(player.getUniqueId());

        player.sendMessage("§aTienes §e" + NumberUtils.formatWithSuffix(tokens) + " §atokens.");
        return true;
    }
}
