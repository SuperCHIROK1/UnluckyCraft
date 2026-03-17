package me.superchirok1.unluckycraft;

import me.superchirok1.unluckycraft.command.UlcCommandExecutor;
import me.superchirok1.unluckycraft.command.UlsCommandExecutor;
import me.superchirok1.unluckycraft.command.tab.UlcTabCompleter;
import me.superchirok1.unluckycraft.gui.ConfigGui;
import me.superchirok1.unluckycraft.listener.OreListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class UnluckyCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new OreListener(this), this);
        PluginCommand ulcCommand = getCommand("ulc");
        if (ulcCommand != null) {
            ulcCommand.setExecutor(new UlcCommandExecutor(this));
            ulcCommand.setTabCompleter(new UlcTabCompleter());
        } else {
            getLogger().warning("Command 'ulc' is missing from plugin.yml");
        }

        ConfigGui configGui = new ConfigGui(this);
        getServer().getPluginManager().registerEvents(configGui, this);
        PluginCommand ulsCommand = getCommand("uls");
        if (ulsCommand != null) {
            ulsCommand.setExecutor(new UlsCommandExecutor(this, configGui));
        } else {
            getLogger().warning("Command 'uls' is missing from plugin.yml");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
