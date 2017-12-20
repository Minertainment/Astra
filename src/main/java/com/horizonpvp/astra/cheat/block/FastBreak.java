package com.horizonpvp.astra.cheat.block;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Material.*;

public class FastBreak extends Cheat implements Listener {

    private static final EnumMap<Material, Double> BLOCK_HARDNESS = new EnumMap<>(Material.class);
    private static final EnumMap<Material, Double> TOOL_HARDNESS = new EnumMap<>(Material.class);
    private static final EnumSet<Material> INSTA_BREAK = EnumSet.of(TORCH, FLOWER_POT, RED_ROSE, YELLOW_FLOWER, LONG_GRASS, RED_MUSHROOM, BROWN_MUSHROOM, TRIPWIRE, TRIPWIRE_HOOK, DEAD_BUSH, DIODE_BLOCK_OFF, DIODE_BLOCK_ON, REDSTONE_COMPARATOR_OFF, REDSTONE_WIRE, REDSTONE_TORCH_OFF, REDSTONE_TORCH_ON, DOUBLE_PLANT, SUGAR_CANE_BLOCK);

    static {
        a(STONE, 1.5D);
        a(GRASS, 0.6D);
        a(DIRT, 0.5D);
        a(COBBLESTONE, 2.0D);
        a(WOOD, 2.0D);
        a(SAPLING, 0.0D);
        a(WATER, 100.0D);
        a(STATIONARY_WATER, 100.0D);
        a(LAVA, 0.0D);
        a(STATIONARY_LAVA, 100.0D);
        a(SAND, 0.5D);
        a(GRAVEL, 0.6D);
        a(GOLD_ORE, 3.0D);
        a(IRON_ORE, 3.0D);
        a(COAL_ORE, 3.0D);
        a(LOG, 2.0D);
        a(LOG_2, 2.0D);
        a(LEAVES, 0.2D);
        a(LEAVES_2, 0.2D);
        a(SPONGE, 0.6D);
        a(GLASS, 0.3D);
        a(LAPIS_ORE, 3.0D);
        a(LAPIS_BLOCK, 3.0D);
        a(DISPENSER, 3.5D);
        a(DROPPER, 3.5D);
        a(SANDSTONE, 0.8D);
        a(NOTE_BLOCK, 0.8D);
        a(BED, 0.2D);
        a(RAILS, 0.7D);
        a(DETECTOR_RAIL, 0.7D);
        a(POWERED_RAIL, 0.7D);
        a(ACTIVATOR_RAIL, 0.7D);
        a(WEB, 4.0D);
        a(LONG_GRASS, 0.0D);
        a(DOUBLE_PLANT, 0.0D);
        a(DEAD_BUSH, 0.0D);
        a(WOOL, 0.8D);
        a(YELLOW_FLOWER, 0.0D);
        a(RED_ROSE, 0.0D);
        a(BROWN_MUSHROOM, 0.0D);
        a(RED_MUSHROOM, 0.0D);
        a(GOLD_BLOCK, 3.0D);
        a(IRON_BLOCK, 5.0D);
        a(DOUBLE_STEP, 2.0D);
        a(STEP, 2.0D);
        a(BRICK, 2.0D);
        a(TNT, 0.0D);
        a(BOOKSHELF, 1.5D);
        a(MOSSY_COBBLESTONE, 2.0D);
        a(OBSIDIAN, 50.0D);
        a(TORCH, 0.0D);
        a(FIRE, 0.0D);
        a(MOB_SPAWNER, 5.0D);
        a(CHEST, 2.5D);
        a(TRAPPED_CHEST, 2.5D);
        a(REDSTONE_WIRE, 0.0D);
        a(DIAMOND_ORE, 3.0D);
        a(DIAMOND_BLOCK, 5.0D);
        a(WORKBENCH, 2.5D);
        a(SOIL, 0.6D);
        a(FURNACE, 3.5D);
        a(BURNING_FURNACE, 3.5D);
        a(WOODEN_DOOR, 3.0D);
        a(LADDER, 0.4D);
        a(LEVER, 0.5D);
        a(IRON_DOOR_BLOCK, 5.0D);
        a(HOPPER, 2.0D);
        a(STONE_PLATE, 0.5D);
        a(WOOD_PLATE, 0.5D);
        a(GOLD_PLATE, 0.5D);
        a(IRON_PLATE, 0.5D);
        a(QUARTZ_BLOCK, 0.8D);
        a(QUARTZ_ORE, 3.0D);
        a(REDSTONE_BLOCK, 5.0D);
        a(REDSTONE_ORE, 3.0D);
        a(GLOWING_REDSTONE_ORE, 3.0D);
        a(REDSTONE_TORCH_OFF, 0.0D);
        a(REDSTONE_TORCH_ON, 0.0D);
        a(REDSTONE_COMPARATOR_OFF, 0.0D);
        a(REDSTONE_COMPARATOR_ON, 0.0D);
        a(DAYLIGHT_DETECTOR, 0.2D);
        a(STONE_BUTTON, 0.5D);
        a(SNOW, 0.1D);
        a(ICE, 0.5D);
        a(PACKED_ICE, 0.5D);
        a(SNOW_BLOCK, 0.2D);
        a(CACTUS, 0.4D);
        a(CLAY, 0.6D);
        a(HARD_CLAY, 1.25D);
        a(STAINED_CLAY, 1.25D);
        a(SUGAR_CANE_BLOCK, 0.0D);
        a(JUKEBOX, 2.0D);
        a(FENCE, 2.0D);
        a(NETHERRACK, 0.4D);
        a(SOUL_SAND, 0.5D);
        a(GLOWSTONE, 0.3D);
        a(PORTAL, -1.0D);
        a(CAKE_BLOCK, 0.5D);
        a(TRAP_DOOR, 3.0D);
        a(MONSTER_EGGS, 0.75D);
        a(SMOOTH_BRICK, 1.5D);
        a(IRON_FENCE, 5.0D);
        a(THIN_GLASS, 0.3D);
        a(STAINED_GLASS, 0.3D);
        a(STAINED_GLASS_PANE, 0.3D);
        a(PUMPKIN_STEM, 0.0D);
        a(MELON_STEM, 0.0D);
        a(VINE, 0.2D);
        a(FENCE_GATE, 2.0D);
        a(MYCEL, 0.6D);
        a(WATER_LILY, 0.0D);
        a(NETHER_BRICK,2.0D);
        a(NETHER_FENCE, 2.0D);
        a(ENCHANTMENT_TABLE, 5.0D);
        a(BREWING_STAND, 0.5D);
        a(CAULDRON, 2.0D);
        a(ENDER_PORTAL, -1.0D);
        a(ENDER_PORTAL_FRAME, -1.0D);
        a(DRAGON_EGG, 3.0D);
        a(REDSTONE_LAMP_OFF, 0.3D);
        a(REDSTONE_LAMP_ON, 0.3D);
        a(WOOD_DOUBLE_STEP, 2.0D);
        a(WOOD_STEP, 2.0D);
        a(COCOA, 0.2D);
        a(EMERALD_ORE, 3.0D);
        a(ENDER_CHEST, 22.5D);
        a(EMERALD_BLOCK, 5.0D);
        a(FLOWER_POT, 0.0D);
        a(WOOD_BUTTON, 0.5D);
        a(ANVIL, 5.0D);
        a(HUGE_MUSHROOM_1, 0.2D);
        a(HUGE_MUSHROOM_2, 0.2D);
        a(BED_BLOCK, 0.2D);
        a(WOOD_STAIRS, 2.0D);
        a(COBBLESTONE_STAIRS, 2.0D);
        a(BRICK_STAIRS, 2.0D);
        a(SMOOTH_STAIRS, 1.5D);
        a(NETHER_BRICK_STAIRS, 2.0D);
        a(SPRUCE_WOOD_STAIRS, 2.0D);
        a(BIRCH_WOOD_STAIRS, 2.0D);
        a(JUNGLE_WOOD_STAIRS, 2.0D);
        a(DARK_OAK_STAIRS, 2.0D);
        a(ACACIA_STAIRS, 2.0D);
        a(QUARTZ_STAIRS, 0.8D);
        a(SANDSTONE_STAIRS, 0.8D);
        a(COMMAND, 0.0D);
        a(BEACON, 3.0D);
        a(COBBLE_WALL, 2.0D);
        a(CROPS, 0.0D);
        a(CARROT, 0.0D);
        a(POTATO, 0.0D);
        a(SKULL, 1.0D);
        a(NETHER_WARTS, 0.0D);
        a(SIGN_POST, 1.0D);
        a(WALL_SIGN, 1.0D);
        a(PUMPKIN, 1.0D);
        a(JACK_O_LANTERN, 1.0D);
        a(DIODE_BLOCK_ON, 0.0D);
        a(DIODE_BLOCK_OFF, 0.0D);
        a(MELON_BLOCK, 1.0D);
        a(ENDER_STONE, 3.0D);
        a(TRIPWIRE, 0.0D);
        a(TRIPWIRE_HOOK, 0.0D);
        a(HAY_BLOCK, 0.5D);
        a(CARPET, 0.1D);
        a(COAL_BLOCK, 5.0D);
        a(PISTON_BASE, 0.7D);
        a(PISTON_EXTENSION, 0.7D);
        a(PISTON_MOVING_PIECE, 0.7D);
        a(PISTON_STICKY_BASE, 0.7D);

        b(WOOD_AXE, 0.75D);
        b(WOOD_PICKAXE, 0.75D);
        b(WOOD_SPADE, 0.75D);
        b(STONE_AXE, 0.4D);
        b(STONE_PICKAXE, 0.4D);
        b(STONE_SPADE, 0.4D);
        b(IRON_AXE, 0.25D);
        b(IRON_PICKAXE, 0.25D);
        b(IRON_SPADE, 0.25D);
        b(DIAMOND_AXE, 0.2D);
        b(DIAMOND_SPADE, 0.2D);
        b(DIAMOND_PICKAXE, 0.2D);
        b(SHEARS, 0.55D);
        b(GOLD_AXE, 0.15D);
        b(GOLD_SPADE, 0.15D);
        b(GOLD_PICKAXE, 0.15D);
    }

    private Map<UUID, Long> lastBreak = new WeakHashMap<>();

    public FastBreak(AntiCheat antiCheat) {
        super(antiCheat, CheatType.FAST_BREAK);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if(p.getGameMode() == GameMode.CREATIVE || p.getItemInHand() == null || p.hasPermission("anticheat.bypass")) {
            return;
        }

        Double blockHardness = BLOCK_HARDNESS.get(event.getBlock().getType());
        if(blockHardness == null) {
            return;
        }

        Double toolHardness = TOOL_HARDNESS.get(p.getItemInHand().getType());
        if(toolHardness == null || INSTA_BREAK.contains(event.getBlock().getType())) {
            return;
        }

        if(blockHardness < 1.5D) {
            return;
        }

        if(p.getItemInHand().getType() == Material.SHEARS && event.getBlock().getType() == Material.LEAVES) {
            return;
        }

        if(p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            return;
        }

        int level = 0;
        if(p.getItemInHand().containsEnchantment(Enchantment.DIG_SPEED)) {
            level = p.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED);
        }

        long result = Math.round(blockHardness * toolHardness * 1000.0D);
        if(level > 0) {
            result /= level * level + 1L;
        }

        result = result > 25000L ? 25000L : (result < 0L ? 0L : result);
        if ((event.getPlayer().getItemInHand().getType().name().endsWith("_SWORD") && event.getBlock().getType() == Material.WEB) || (event.getPlayer().getItemInHand().getType() == Material.SHEARS && event.getBlock().getType() == Material.WOOL)) {
            result /= 2L;
        }

        if (lastBreak.containsKey(event.getPlayer().getUniqueId())) {
            long dm = TimeUnit.MILLISECONDS.convert(System.nanoTime() - lastBreak.get(event.getPlayer().getUniqueId()), TimeUnit.NANOSECONDS);
            if (dm < result) {
                if(getConfig().getBoolean("block")) {
                    event.setCancelled(true);
                }
                fail(event.getPlayer(), Probability.HIGH);
            }
        }

        lastBreak.put(event.getPlayer().getUniqueId(), System.nanoTime());

    }

    private static void a(Material m, Double d) {
        BLOCK_HARDNESS.put(m,d);
    }

    private static void b(Material m, Double d) {
        TOOL_HARDNESS.put(m,d);
    }

}
