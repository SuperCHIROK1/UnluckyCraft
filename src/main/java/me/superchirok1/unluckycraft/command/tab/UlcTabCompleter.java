package me.superchirok1.unluckycraft.command.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UlcTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("about", "reload");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        String prefix = args[0].toLowerCase();
        List<String> matches = new ArrayList<>();
        for (String option : SUBCOMMANDS) {
            if (option.startsWith(prefix)) {
                matches.add(option);
            }
        }

        return matches;
    }
}
