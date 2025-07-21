package me.superchirok1.unluckycraft;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class OreListener implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    private final Map<Material, OreDrop> oreDrops = new EnumMap<>(Material.class);

    public OreListener(JavaPlugin plugin) {
        this.plugin = plugin;

        registerOre("DIAMOND_ORE", "diamond", Material.valueOf(plugin.getConfig().getString("drops.diamond.material")));
        registerOre("DEEPSLATE_DIAMOND_ORE", "diamond", Material.valueOf(plugin.getConfig().getString("drops.diamond.material")));

        registerOre("EMERALD_ORE", "emerald", Material.valueOf(plugin.getConfig().getString("drops.emerald.material")));
        registerOre("DEEPSLATE_EMERALD_ORE", "emerald", Material.valueOf(plugin.getConfig().getString("drops.emerald.material")));

        registerOre("GOLD_ORE", "gold", Material.valueOf(plugin.getConfig().getString("drops.gold.material")));
        registerOre("DEEPSLATE_GOLD_ORE", "gold", Material.valueOf(plugin.getConfig().getString("drops.gold.material")));
        registerOre("NETHER_GOLD_ORE", "gold", Material.valueOf(plugin.getConfig().getString("drops.gold.material")));

        registerOre("IRON_ORE", "iron", Material.valueOf(plugin.getConfig().getString("drops.iron.material")));
        registerOre("DEEPSLATE_IRON_ORE", "iron", Material.valueOf(plugin.getConfig().getString("drops.iron.material")));

        registerOre("COAL_ORE", "coal", Material.valueOf(plugin.getConfig().getString("drops.coal.material")));
        registerOre("DEEPSLATE_COAL_ORE", "coal", Material.valueOf(plugin.getConfig().getString("drops.coal.material")));

        registerOre("NETHER_QUARTZ_ORE", "quartz", Material.valueOf(plugin.getConfig().getString("drops.quartz.material")));
        registerOre("ANCIENT_DEBRIS", "netherite", Material.valueOf(plugin.getConfig().getString("drops.netherite.material")));
    }

    private void registerOre(String blockMaterialName, String configKey, Material dropItem) {
        try {
            Material blockMaterial = Material.valueOf(blockMaterialName);
            String displayName = plugin.getConfig().getString("drops." + configKey+ ".name", "Предмет");
            oreDrops.put(blockMaterial, new OreDrop(configKey, new ItemStack(dropItem), displayName));
        } catch (IllegalArgumentException ignored) {}
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();

        OreDrop drop = oreDrops.get(blockType);
        if (drop == null) return;

        event.setDropItems(false);

        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        FileConfiguration config = plugin.getConfig();

        double chance = config.getDouble("chances." + drop.configKey, 0.1);
        boolean isLucky = random.nextDouble() < chance;

        String messageKey = isLucky ? "messages.lucky" : "messages.unlucky";
        String message = config.getString(messageKey, isLucky ? "Вы получили {item}" : "Вам не повезло");
        message = message.replace("{item}", drop.displayName);

        String soundKey = isLucky ? "sounds.lucky" : "sounds.unlucky";
        String soundName = config.getString(soundKey, "BLOCK_NOTE_BLOCK_PLING");

        Sound sound = Sound.valueOf(soundName);

        player.playSound(player.getLocation(), sound, 1.0F, isLucky ? 1.5F : 0.9F);
        player.sendMessage(ColorUtils.translateHex(message));

        if (isLucky) {
            block.getWorld().dropItemNaturally(block.getLocation(), drop.item);
        }
    }

    private static class OreDrop {
        final String configKey;
        final ItemStack item;
        final String displayName;

        public OreDrop(String configKey, ItemStack item, String displayName) {
            this.configKey = configKey;
            this.item = item;
            this.displayName = displayName;
        }
    }
}
