package com.stp.commands.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TokenTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList(
        "add", "remove", "set", "balance", "enchant", "give", "pay"
    );
    private static final List<String> ENCHANT_SUBS = Arrays.asList(
        "set", "nextlevel", "downlevel"
    );

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (String sub : SUBCOMMANDS) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
            if (args[0].isEmpty()) {
                completions.addAll(SUBCOMMANDS);
            }
            return completions;
        }

        else if (args.length == 2 && args[0].equalsIgnoreCase("enchant")) {
            List<String> completions = new ArrayList<>();
            for (String sub : ENCHANT_SUBS) {
                if (sub.startsWith(args[1].toLowerCase())) {
                    completions.add(sub);
                }
            }
            if (args[1].isEmpty()) {
                completions.addAll(ENCHANT_SUBS);
            }
            return completions;
        }

        else if (args.length == 2) {
            if (Arrays.asList("add", "remove", "set", "balance", "give", "pay").contains(args[0].toLowerCase())) {
                List<String> playerNames = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        playerNames.add(p.getName());
                    }
                }
                return playerNames;
            }
        }

        else if (args.length == 3 && args[0].equalsIgnoreCase("enchant")) {
            List<String> playerNames = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                    playerNames.add(p.getName());
                }
            }
            return playerNames;
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("enchant")) {
            return Arrays.asList("speed", "explosive", "efficiency", "fortune", "fly", "nuke", "give-token");
        }
        return Collections.emptyList();
    }
}