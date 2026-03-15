package me.superchirok1.unluckycraft.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.superchirok1.unluckycraft.util.ColorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGui implements Listener {

    private final JavaPlugin plugin;

    private final List<String> ores = Arrays.asList("diamond", "emerald", "gold", "iron", "coal", "quartz", "netherite");
    private final Map<String, Material> icons = new HashMap<>();

    public ConfigGui(JavaPlugin plugin) {
        this.plugin = plugin;
        loadIcons();
    }

    public void open(Player player) {
        String rawTitle = plugin.getConfig().getString("gui.title", "&7Настройка шансов дропа");
        Inventory gui = Bukkit.createInventory(null, 9 * 3, ColorUtils.translateHex(rawTitle));

        int startSlot = 10;

        for (int i = 0; i < ores.size(); i++) {
            String key = ores.get(i);
            double chance = plugin.getConfig().getDouble("chances." + key, 0.1);
            String displayName = plugin.getConfig().getString("drops." + key + ".name", key.toUpperCase());

            String lmb = plugin.getConfig().getString("gui.lore.lmb", "&#00FF00ЛКМ: +0.05");
            String rmb = plugin.getConfig().getString("gui.lore.rmb", "&#FF0000ПКМ: -0.05");

            Material material = icons.getOrDefault(key, Material.STONE);
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ColorUtils.translateHex("&f" + displayName));

            List<String> lore = Arrays.asList(
                    "&8" + String.format("%.2f", chance),
                    lmb,
                    rmb
            );

            List<String> translatedLore = new ArrayList<>();
            for (String line : lore) {
                translatedLore.add(ColorUtils.translateHex(line));
            }

            meta.setLore(translatedLore);
            item.setItemMeta(meta);

            gui.setItem(startSlot + i, item);
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String rawTitle = plugin.getConfig().getString("gui.title", "&7Настройка шансов дропа");
        if (!event.getView().getTitle().equals(ColorUtils.translateHex(rawTitle))) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String name = ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).toLowerCase();
        String key = null;

        for (String ore : ores) {
            String display = plugin.getConfig().getString("drops." + ore + ".name", ore.toUpperCase());
            if (display.equals(name)) {
                key = ore;
                break;
            }
        }

        if (key == null) return;

        double current = plugin.getConfig().getDouble("chances." + key, 0.1);

        switch (event.getClick()) {
            case LEFT:
                current = Math.min(1.0, current + 0.05);
                break;
            case RIGHT:
                current = Math.max(0.0, current - 0.05);
                break;
            default:
                return;
        }

        plugin.getConfig().set("chances." + key, current);
        plugin.saveConfig();

        String chanceMessage = plugin.getConfig().getString(
                "messages.gui.chance-updated",
                "&fᴄʜᴀɴᴄᴇ ꜰᴏʀ &e{ore} &fsᴇᴛ ᴛᴏ &e{chance}&e%"
        );
        chanceMessage = chanceMessage
                .replace("{ore}", key)
                .replace("{chance}", String.format("%.2f", current));
        player.sendMessage(ColorUtils.translateHex(chanceMessage));
        open(player);
    }

    private void loadIcons() {
        for (String ore : ores) {
            String path = "drops." + ore + ".material";
            String materialName = plugin.getConfig().getString(path);
            if (materialName == null) {
                plugin.getLogger().warning("Missing material for " + path);
                continue;
            }
            Material material = parseMaterial(materialName, path);
            if (material != null) {
                icons.put(ore, material);
            }
        }
    }

    private Material parseMaterial(String materialName, String path) {
        try {
            return Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid material '" + materialName + "' for " + path);
            return null;
        }
    }
}
