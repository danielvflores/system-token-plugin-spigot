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

public class TokenAdd implements SubCommand {

    private final TokenManager tokenManager;

    public TokenAdd(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("token.add")) {
            sender.sendMessage(MessageUtils.getMessage("general.no-permission"));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(MessageUtils.getMessage("token.add-usage"));
            return true;
        }

        String targetName = args[1];
        Player target = sender.getServer().getPlayerExact(targetName);

        if (target == null) {
            sender.sendMessage(MessageUtils.getMessage("token.player-not-found"));
            return true;
        }

        BigDecimal amount;
        try {
            amount = NumberUtils.parseAmountWithSuffix(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtils.getMessage("token.invalid-amount"));
            return true;
        }

        tokenManager.addTokens(target.getUniqueId(), amount);

        BigDecimal newBalance = tokenManager.getTokens(target.getUniqueId());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("amount", amount.toPlainString());
        placeholders.put("player", target.getName());

        sender.sendMessage(PlaceholderUtil.applyPlaceholders(
                sender instanceof Player ? (Player)sender : null,
                MessageUtils.getMessage("token.add-success"),
                placeholders
        ));

        Map<String, String> placeholdersTarget = new HashMap<>();
        placeholdersTarget.put("balance", newBalance.toPlainString());
        target.sendMessage(PlaceholderUtil.applyPlaceholders(
                target,
                MessageUtils.getMessage("token.new-balance"),
                placeholdersTarget
        ));
        return true;
    }
}