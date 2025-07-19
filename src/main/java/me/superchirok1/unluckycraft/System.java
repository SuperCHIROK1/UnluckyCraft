package me.superchirok1.unluckycraft;

import me.superchirok1.unluckycraft.UnluckyCraft;
import me.superchirok1.unluckycraft.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class System implements CommandExecutor {


    private final String prfx;

    private final UnluckyCraft plugin;

    public System(UnluckyCraft plugin) {
        this.plugin = plugin;
        this.prfx = "&#FFD900&lU&#F2DF00&ln&#E6E600&ll&#D9EC00&lu&#CCF200&lc&#C0F900&lk&#B3FF00&ly&#C2FF00&lC&#D0FF00&lr&#DFFF00&la&#EDFF00&lf&#FCFF00&lt";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("about")) {
            sender.sendMessage(ColorUtils.translateHex(" "));
            sender.sendMessage(ColorUtils.translateHex(" " + prfx + " &7(&#FFD900" + plugin.getDescription().getVersion() + "&7)"));
            sender.sendMessage(ColorUtils.translateHex(" &7Unlucky plugin"));
            sender.sendMessage(ColorUtils.translateHex(" "));
            sender.sendMessage(ColorUtils.translateHex(" &fBy: &#FFD900SuperCHIROK1"));
            sender.sendMessage(ColorUtils.translateHex(" "));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("ulc.reload")) {
                sender.sendMessage(ColorUtils.translateHex("&7[" + prfx + "&7] &cNot have perms!"));
            } else {
                plugin.reloadConfig();
                sender.sendMessage(ColorUtils.translateHex("&7[" + prfx + "&7] &fReloaded!"));
            }
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateHex(" "));
        sender.sendMessage(ColorUtils.translateHex(" " + prfx + " &7(&#FFD900" + plugin.getDescription().getVersion() + "&7)"));
        sender.sendMessage(ColorUtils.translateHex(" &8&m    &f"));
        sender.sendMessage(ColorUtils.translateHex(" &#FFD900/ulc about &7- &fAbout plugin"));
        sender.sendMessage(ColorUtils.translateHex(" &#FFD900/ulc reload &7- &fReloads plugin"));
        sender.sendMessage(ColorUtils.translateHex(" &#FFD900/uls &7- &fSetting the drop chances"));
        sender.sendMessage(ColorUtils.translateHex(" "));
    }
}
