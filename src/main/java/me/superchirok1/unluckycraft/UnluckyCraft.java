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
                configGui.open((Player) sender);
                return true;
            }
            sender.sendMessage("§cТолько игрок может открыть GUI.");
            return true;
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
