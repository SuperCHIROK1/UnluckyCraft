package me.superchirok1.unluckycraft.command;

import me.superchirok1.unluckycraft.UnluckyCraft;
import me.superchirok1.unluckycraft.gui.ConfigGui;
import me.superchirok1.unluckycraft.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UlsCommandExecutor implements CommandExecutor {

    private final UnluckyCraft plugin;
    private final ConfigGui configGui;

    public UlsCommandExecutor(UnluckyCraft plugin, ConfigGui configGui) {
        this.plugin = plugin;
        this.configGui = configGui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String noPermission = plugin.getConfig().getString("messages.errors.no-permission", "&cNot has permission");
        String onlyPlayers = plugin.getConfig().getString("messages.errors.only-players", "&cOnly players can use GUI");
        if (sender instanceof Player) {
            if (sender.hasPermission("ulc.settings")) {
                configGui.open((Player) sender);
                return true;
            }
            sender.sendMessage(ColorUtils.translateHex(noPermission));
            return true;
        }
        sender.sendMessage(ColorUtils.translateHex(onlyPlayers));
        return true;
    }
}
