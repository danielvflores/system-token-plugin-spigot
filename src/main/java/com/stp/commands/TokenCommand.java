package com.stp.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.stp.commands.economy.TokenAdd;
import com.stp.commands.economy.TokenBalance;
import com.stp.commands.economy.TokenRemove;
import com.stp.commands.economy.TokenSet;
import com.stp.commands.enchant.TokenEnchant;
import com.stp.commands.test.TokenCheckConsole;
import com.stp.economy.TokenManager;

public class TokenCommand implements CommandExecutor {

    private static final String BALANCE = "balance";
    private static final String SET = "set";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String CHECK = "tokencheck";
    private static final String ENCHANT = "enchant";

    private final Map<String, SubCommand> subcommands = new HashMap<>();
    private final TokenManager tokenManager;

    public TokenCommand(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        registerSubcommands();
    }

    private void registerSubcommands() {
        register(BALANCE, new TokenBalance(tokenManager));
        register(SET, new TokenSet(tokenManager));
        register(ADD, new TokenAdd(tokenManager));
        register(REMOVE, new TokenRemove(tokenManager));
        register(CHECK, new TokenCheckConsole(tokenManager));
        register(ENCHANT, new TokenEnchant());
    }

    private void register(String name, SubCommand sub) {
        subcommands.put(name.toLowerCase(), sub);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUsa /token help");
            return true;
        }

        String subcommandName = args[0].toLowerCase();

        SubCommand subcommand = subcommands.get(subcommandName);
        if (subcommand != null) {
            return subcommand.execute(sender, args);
        }

        sender.sendMessage("§cSubcomando desconocido. Usa /token help");
        return true;
    }
}
