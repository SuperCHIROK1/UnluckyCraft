package me.superchirok1.unluckycraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ConfigGui implements Listener {

    private final JavaPlugin plugin;
    private final String GUI_TITLE = "&7Настройка шансов дропа";

    private final List<String> ores = Arrays.asList("diamond", "emerald", "gold", "iron", "coal", "quartz", "netherite");

    private final Map<String, Material> icons = Map.of(
            "diamond", Material.DIAMOND,
            "emerald", Material.EMERALD,
            "gold", Material.GOLD_INGOT,
            "iron", Material.IRON_INGOT,
            "coal", Material.COAL,
            "quartz", Material.QUARTZ,
            "netherite", Material.NETHERITE_SCRAP
    );

    public ConfigGui(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9 * 3, ColorUtils.translateHex(GUI_TITLE));

        for (int i = 0; i < ores.size(); i++) {
            String key = ores.get(i);
            double chance = plugin.getConfig().getDouble("chances." + key, 0.1);

            ItemStack item = new ItemStack(icons.getOrDefault(key, Material.STONE));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§e" + key.toUpperCase());
            meta.setLore(Arrays.asList("§7Текущий шанс: §f" + String.format("%.2f", chance),
                    "§aЛКМ: +0.05", "§cПКМ: -0.05"));
            item.setItemMeta(meta);

            gui.setItem(i, item);
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String name = clicked.getItemMeta().getDisplayName();
        if (!name.startsWith("§e")) return;

        String key = name.substring(2).toLowerCase();
        if (!ores.contains(key)) return;

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

        player.sendMessage("§aШанс для §e" + key + " §aустановлен на §f" + String.format("%.2f", current));
        open(player);
    }
}
