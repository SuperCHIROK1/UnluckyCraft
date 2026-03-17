package me.superchirok1.unluckycraft.listener;

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

import me.superchirok1.unluckycraft.util.ColorUtils;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class OreListener implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    private final Map<Material, OreDrop> oreDrops = new EnumMap<>(Material.class);

    public OreListener(JavaPlugin plugin) {
        this.plugin = plugin;

        registerOre("DIAMOND_ORE", "diamond");
        registerOre("DEEPSLATE_DIAMOND_ORE", "diamond");

        registerOre("EMERALD_ORE", "emerald");
        registerOre("DEEPSLATE_EMERALD_ORE", "emerald");

        registerOre("GOLD_ORE", "gold");
        registerOre("DEEPSLATE_GOLD_ORE", "gold");
        registerOre("NETHER_GOLD_ORE", "gold");

        registerOre("IRON_ORE", "iron");
        registerOre("DEEPSLATE_IRON_ORE", "iron");

        registerOre("COAL_ORE", "coal");
        registerOre("DEEPSLATE_COAL_ORE", "coal");

        registerOre("NETHER_QUARTZ_ORE", "quartz");
        registerOre("ANCIENT_DEBRIS", "netherite");
    }

    private void registerOre(String blockMaterialName, String configKey) {
        Material blockMaterial = parseMaterial(blockMaterialName, "block material");
        if (blockMaterial == null) {
            return;
        }

        String dropPath = "drops." + configKey + ".material";
        String dropMaterialName = plugin.getConfig().getString(dropPath);
        if (dropMaterialName == null) {
            plugin.getLogger().warning("Missing material for " + dropPath);
            return;
        }

        Material dropMaterial = parseMaterial(dropMaterialName, dropPath);
        if (dropMaterial == null) {
            return;
        }

        String displayName = plugin.getConfig().getString("drops." + configKey + ".name", "Предмет");
        oreDrops.put(blockMaterial, new OreDrop(configKey, new ItemStack(dropMaterial), displayName));
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

        Sound sound = null;
        try {
            sound = Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound '" + soundName + "' for " + soundKey);
        }

        if (sound != null) {
            player.playSound(player.getLocation(), sound, 1.0F, isLucky ? 1.5F : 0.9F);
        }
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

    private Material parseMaterial(String materialName, String context) {
        try {
            return Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid material '" + materialName + "' for " + context);
            return null;
        }
    }
}
