package me.superchirok1.unluckycraft.command;

import me.superchirok1.unluckycraft.UnluckyCraft;
import me.superchirok1.unluckycraft.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class UlcCommandExecutor implements CommandExecutor {

    private static final String DEFAULT_PREFIX = "&#FFD900&lU&#F2DF00&ln&#E6E600&ll&#D9EC00&lu&#CCF200&lc&#C0F900&lk&#B3FF00&ly&#C2FF00&lC&#D0FF00&lr&#DFFF00&la&#EDFF00&lf&#FCFF00&lt";

    private final UnluckyCraft plugin;

    public UlcCommandExecutor(UnluckyCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("about")) {
            sendLines(sender, getConfiguredLines(
                    "messages.commands.about.lines",
                    Arrays.asList(
                            " ",
                            " {prefix} &7(&#FFD900{version}&7)",
                            " &7Unlucky plugin",
                            " ",
                            " &fBy: &#FFD900SuperCHIROK1",
                            " "
                    )
            ));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            String reloadNoPermission = plugin.getConfig().getString(
                    "messages.commands.reload.no-permission",
                    "&7[{prefix}&7] &cNot have perms!"
            );
            String reloadSuccess = plugin.getConfig().getString(
                    "messages.commands.reload.success",
                    "&7[{prefix}&7] &fReloaded!"
            );
            if (!sender.hasPermission("ulc.reload")) {
                sender.sendMessage(ColorUtils.translateHex(applyPlaceholders(reloadNoPermission)));
            } else {
                plugin.reloadConfig();
                sender.sendMessage(ColorUtils.translateHex(applyPlaceholders(reloadSuccess)));
            }
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sendLines(sender, getConfiguredLines(
                "messages.commands.help.lines",
                Arrays.asList(
                        " ",
                        " {prefix} &7(&#FFD900{version}&7)",
                        " &8&m    &f",
                        " &#FFD900/ulc about &7- &fAbout plugin",
                        " &#FFD900/ulc reload &7- &fReloads plugin",
                        " &#FFD900/uls &7- &fSetting the drop chances",
                        " "
                )
        ));
    }

    private List<String> getConfiguredLines(String path, List<String> defaults) {
        List<String> lines = plugin.getConfig().getStringList(path);
        if (lines == null || lines.isEmpty()) {
            return defaults;
        }
        return lines;
    }

    private void sendLines(CommandSender sender, List<String> lines) {
        for (String line : lines) {
            sender.sendMessage(ColorUtils.translateHex(applyPlaceholders(line)));
        }
    }

    private String applyPlaceholders(String message) {
        if (message == null) {
            return "";
        }
        return message
                .replace("{version}", plugin.getDescription().getVersion())
                .replace("{prefix}", getPrefix());
    }

    private String getPrefix() {
        return plugin.getConfig().getString("messages.prefix", DEFAULT_PREFIX);
    }
}
