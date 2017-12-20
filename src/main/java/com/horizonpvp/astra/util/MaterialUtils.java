package com.horizonpvp.astra.util;

import org.bukkit.Material;

import java.util.EnumSet;

import static org.bukkit.Material.*;

public final class MaterialUtils {

    private static final EnumSet<Material> passable = EnumSet.of(ACACIA_STAIRS, ACTIVATOR_RAIL, AIR, ANVIL, ARMOR_STAND, BANNER, BEACON, BED_BLOCK, BIRCH_WOOD_STAIRS, BREWING_STAND, BRICK_STAIRS, BROWN_MUSHROOM, CACTUS, CAKE_BLOCK, CARPET, CARROT, CAULDRON, CHEST, COBBLESTONE_STAIRS, COCOA, CROPS, DARK_OAK_STAIRS, DAYLIGHT_DETECTOR, DAYLIGHT_DETECTOR_INVERTED, DEAD_BUSH, DETECTOR_RAIL, DIODE_BLOCK_OFF, DIODE_BLOCK_ON, DOUBLE_PLANT, DRAGON_EGG, ENCHANTMENT_TABLE, ENDER_CHEST, ENDER_PORTAL, ENDER_PORTAL_FRAME, FIRE, FLOWER_POT, GOLD_PLATE, HOPPER, IRON_PLATE, JUNGLE_WOOD_STAIRS, LADDER, LAVA, LEVER, LONG_GRASS, MELON_STEM, NETHER_BRICK_STAIRS, NETHER_WARTS, PISTON_BASE, PISTON_EXTENSION, PISTON_MOVING_PIECE, PISTON_STICKY_BASE, PORTAL, POTATO, POWERED_RAIL, PUMPKIN_STEM, QUARTZ_STAIRS, RAILS, RED_MUSHROOM, RED_ROSE, RED_SANDSTONE_STAIRS, REDSTONE_COMPARATOR_OFF, REDSTONE_COMPARATOR_ON, REDSTONE_TORCH_OFF, REDSTONE_TORCH_ON, REDSTONE_WIRE, SANDSTONE_STAIRS, SAPLING, SEEDS, SIGN, SIGN_POST, SKULL, SMOOTH_STAIRS, SNOW, SOUL_SAND, SPRUCE_WOOD_STAIRS, STANDING_BANNER, STATIONARY_LAVA, STATIONARY_WATER, STEP, STONE_BUTTON, STONE_PLATE, STONE_SLAB2, SUGAR_CANE_BLOCK, TORCH, TRAPPED_CHEST, TRIPWIRE, TRIPWIRE_HOOK, VINE, WALL_BANNER, WALL_SIGN, WATER, WATER_LILY, WEB, WOOD_BUTTON, WOOD_PLATE, WOOD_STAIRS, WOOD_STEP, YELLOW_FLOWER);
    private static final EnumSet<Material> doors = EnumSet.of(ACACIA_DOOR, BIRCH_DOOR, DARK_OAK_DOOR, IRON_DOOR_BLOCK, JUNGLE_DOOR, SPRUCE_DOOR, WOODEN_DOOR);
    private static final EnumSet<Material> gates = EnumSet.of(ACACIA_FENCE_GATE, BIRCH_FENCE_GATE, DARK_OAK_FENCE_GATE, FENCE_GATE, JUNGLE_FENCE_GATE, SPRUCE_FENCE_GATE);
    private static final EnumSet<Material> trapdoors = EnumSet.of(TRAP_DOOR, IRON_TRAPDOOR);
    private static final EnumSet<Material> fences = EnumSet.of(ACACIA_FENCE, BIRCH_FENCE, COBBLE_WALL, DARK_OAK_FENCE, FENCE, IRON_FENCE, JUNGLE_FENCE, NETHER_FENCE, SPRUCE_FENCE, STAINED_GLASS_PANE, THIN_GLASS);
    private static final EnumSet<Material> slabs = EnumSet.of(ACACIA_STAIRS, BIRCH_WOOD_STAIRS, BRICK_STAIRS, COBBLESTONE_STAIRS, DARK_OAK_STAIRS, JUNGLE_WOOD_STAIRS, NETHER_BRICK_STAIRS, QUARTZ_STAIRS, RED_SANDSTONE_STAIRS, SANDSTONE_STAIRS, SMOOTH_STAIRS, WOOD_STAIRS, SPRUCE_WOOD_STAIRS, STEP, SMOOTH_BRICK, STONE_SLAB2, DOUBLE_STEP, WOOD_DOUBLE_STEP, WOOD_STEP);
    private static final EnumSet<Material> door_blocks = EnumSet.of(WOODEN_DOOR, IRON_DOOR_BLOCK, TRAP_DOOR, IRON_TRAPDOOR, SPRUCE_DOOR, BIRCH_DOOR, JUNGLE_DOOR, ACACIA_DOOR, DARK_OAK_DOOR);
    private static final EnumSet<Material> full_blocks = EnumSet.of(STONE, GRASS, DIRT, COBBLESTONE, WOOD, BEDROCK, SAND, GRAVEL, GOLD_ORE, IRON_ORE, COAL_ORE, LOG, LEAVES, SPONGE, GLASS, LAPIS_ORE, LAPIS_BLOCK, DISPENSER, SANDSTONE, WOOL, GOLD_BLOCK, IRON_BLOCK, DOUBLE_STEP, BRICK, TNT, BOOKSHELF, MOSSY_COBBLESTONE, OBSIDIAN, MOB_SPAWNER, DIAMOND_ORE, DIAMOND_BLOCK, WORKBENCH, FURNACE, BURNING_FURNACE, REDSTONE_ORE, GLOWING_REDSTONE_ORE, ICE, SNOW_BLOCK, CLAY, JUKEBOX, PUMPKIN, NETHERRACK, GLOWSTONE, JACK_O_LANTERN, STAINED_GLASS, SMOOTH_BRICK, HUGE_MUSHROOM_1, HUGE_MUSHROOM_2, MELON_BLOCK, MYCEL, NETHER_BRICK, ENDER_STONE, WOOD_DOUBLE_STEP, EMERALD_ORE, EMERALD_BLOCK, COMMAND, REDSTONE_BLOCK, QUARTZ_ORE, QUARTZ_BLOCK, DROPPER, STAINED_CLAY, LEAVES_2, LOG_2, BARRIER, PRISMARINE, SEA_LANTERN, HAY_BLOCK, HARD_CLAY, COAL_BLOCK, PACKED_ICE, RED_SANDSTONE, DOUBLE_STONE_SLAB2);
    private static final EnumSet<Material> openable = EnumSet.of(CHEST, TRAPPED_CHEST, DROPPER, HOPPER, DISPENSER);

    public static boolean isDoorBlock(Material material) {
        return door_blocks.contains(material);
    }

    public static boolean isFullBlock(Material material) {
        return full_blocks.contains(material);
    }

    public static boolean isFence(Material material) {
        return fences.contains(material);
    }

    public static boolean isSlab(Material material) {
        return slabs.contains(material);
    }

    public static boolean isTrapdoor(Material material) {
        return trapdoors.contains(material);
    }

    public static boolean isGate(Material material) {
        return gates.contains(material);
    }

    public static boolean isDoor(Material material) {
        return doors.contains(material);
    }

    public static boolean isPassable(Material material) {
        return passable.contains(material);
    }

    public static boolean isLiquid(Material material) {
        return material == Material.LAVA || material == Material.STATIONARY_LAVA || material == Material.WATER || material == Material.STATIONARY_WATER;
    }

    public static boolean isOpenable(Material material) {
        return openable.contains(material);
    }
}

