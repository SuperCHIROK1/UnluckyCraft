package me.superchirok1.unluckycraft;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class UnluckyCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new OreListener(this), this);
        getCommand("ulc").setExecutor(new System(this));

        ConfigGui configGui = new ConfigGui(this);
        getServer().getPluginManager().registerEvents(configGui, this);

        getCommand("uls").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                if (sender.hasPermission("ulc.settings")) {
                    configGui.open((Player) sender);
                    return true;
                } else {
                    sender.sendMessage("§cNot has permission");
                }
            }
            sender.sendMessage("§cOnly players can use GUI");
            return true;
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
