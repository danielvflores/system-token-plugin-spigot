package com.stp.commands.economy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.stp.commands.SubCommand;
import com.stp.economy.TokenManager;
import com.stp.utils.MessageUtils;
import com.stp.utils.NumberUtils;
import com.stp.utils.PlaceholderUtil;

public class TokenBalance implements SubCommand {

    private final TokenManager tokenManager;

    public TokenBalance(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.getMessage("token.only-player"));
            return true;
        }
        Player player = (Player) sender;
        BigDecimal tokens = tokenManager.getTokens(player.getUniqueId());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("balance", NumberUtils.formatWithSuffix(tokens));

        player.sendMessage(PlaceholderUtil.applyPlaceholders(
            player,
            MessageUtils.getMessage("token.self-balance"),
            placeholders
        ));
        return true;
    }
}