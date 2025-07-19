package me.superchirok1.unluckycraft;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
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

    private final String unlucky = "&#940202&lʜ&#A60202&lᴇ&#B80101&lу&#CA0101&lд&#DB0101&lᴀ&#ED0000&lч&#FF0000&lᴀ";
    private final String lucky = "&#56FF00&lу&#41FF26&lд&#2BFF4C&lᴀ&#16FF72&lч&#00FF98&lᴀ";

    private final Map<Material, OreDrop> oreDrops = new EnumMap<>(Material.class);

    public OreListener(JavaPlugin plugin) {
        this.plugin = plugin;
        registerOre("DIAMOND_ORE", "diamond", Material.DIAMOND, "алмаз");
        registerOre("DEEPSLATE_DIAMOND_ORE", "diamond", Material.DIAMOND, "алмаз");

        registerOre("EMERALD_ORE", "emerald", Material.EMERALD, "изумруд");
        registerOre("DEEPSLATE_EMERALD_ORE", "emerald", Material.EMERALD, "изумруд");

        registerOre("GOLD_ORE", "gold", Material.GOLD_INGOT, "золотой слиток");
        registerOre("DEEPSLATE_GOLD_ORE", "gold", Material.GOLD_INGOT, "золотой слиток");
        registerOre("NETHER_GOLD_ORE", "gold", Material.GOLD_NUGGET, "золотой самородок");

        registerOre("IRON_ORE", "iron", Material.IRON_INGOT, "железный слиток");
        registerOre("DEEPSLATE_IRON_ORE", "iron", Material.IRON_INGOT, "железный слиток");

        registerOre("COAL_ORE", "coal", Material.COAL, "уголь");
        registerOre("DEEPSLATE_COAL_ORE", "coal", Material.COAL, "уголь");

        registerOre("NETHER_QUARTZ_ORE", "quartz", Material.QUARTZ, "кварц");
        registerOre("ANCIENT_DEBRIS", "netherite", Material.NETHERITE_SCRAP, "незеритовый лом");
    }

    private void registerOre(String materialName, String configKey, Material result, String displayName) {
        try {
            Material material = Material.valueOf(materialName);
            oreDrops.put(material, new OreDrop(configKey, result, displayName));
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
        double chance = plugin.getConfig().getDouble("chances." + drop.configKey, 0.1);

        if (player.getGameMode() == GameMode.SURVIVAL) {
            if (random.nextDouble() < chance) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop.item);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.5F);
                player.sendMessage(ColorUtils.translateHex(lucky + " &7• &fВам повезло! &#56FF00Выпал &l" + drop.displayName));
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0F, 0.9F);
                player.sendMessage(ColorUtils.translateHex(unlucky + " &7• &fВам не повезло"));
            }
        }


    }

    private static class OreDrop {
        final String configKey;
        final ItemStack item;
        final String displayName;

        public OreDrop(String configKey, Material resultMaterial, String displayName) {
            this.configKey = configKey;
            this.item = new ItemStack(resultMaterial, 1);
            this.displayName = displayName;
        }
    }
}
