package com.horizonpvp.astra.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

import static org.bukkit.Material.*;

public class CheckUtils {

    private static final EnumSet<Material> SLABS = EnumSet.of(ACACIA_STAIRS, BIRCH_WOOD_STAIRS, BRICK_STAIRS, COBBLESTONE_STAIRS, DARK_OAK_STAIRS, JUNGLE_WOOD_STAIRS, NETHER_BRICK_STAIRS, QUARTZ_STAIRS, RED_SANDSTONE_STAIRS, SANDSTONE_STAIRS, SMOOTH_STAIRS, WOOD_STAIRS, SPRUCE_WOOD_STAIRS, STEP, SMOOTH_BRICK, STONE_SLAB2, DOUBLE_STEP, WOOD_DOUBLE_STEP, WOOD_STEP);
    private static final EnumSet<Material> NON_SOLID = EnumSet.of(AIR, LAVA, WATER, STATIONARY_LAVA, STATIONARY_WATER, GRASS, LONG_GRASS, RED_ROSE, YELLOW_FLOWER);
    private static final EnumSet<Material> NON_FULL = EnumSet.of(LADDER, COBBLE_WALL, FENCE_GATE, FENCE);
    private static final EnumSet<Material> PASSABLE = EnumSet.of(ACACIA_STAIRS, ACTIVATOR_RAIL, AIR, ANVIL, ARMOR_STAND, BANNER, BEACON, BED_BLOCK, BIRCH_WOOD_STAIRS, BREWING_STAND, BRICK_STAIRS, BROWN_MUSHROOM, CACTUS, CAKE_BLOCK, CARPET, CARROT, CAULDRON, CHEST, COBBLESTONE_STAIRS, COCOA, CROPS, DARK_OAK_STAIRS, DAYLIGHT_DETECTOR, DAYLIGHT_DETECTOR_INVERTED, DEAD_BUSH, DETECTOR_RAIL, DIODE_BLOCK_OFF, DIODE_BLOCK_ON, DOUBLE_PLANT, DRAGON_EGG, ENCHANTMENT_TABLE, ENDER_CHEST, ENDER_PORTAL, ENDER_PORTAL_FRAME, FIRE, FLOWER_POT, GOLD_PLATE, HOPPER, IRON_PLATE, JUNGLE_WOOD_STAIRS, LADDER, LAVA, LEVER, LONG_GRASS, MELON_STEM, NETHER_BRICK_STAIRS, NETHER_WARTS, PISTON_BASE, PISTON_EXTENSION, PISTON_MOVING_PIECE, PISTON_STICKY_BASE, PORTAL, POTATO, POWERED_RAIL, PUMPKIN_STEM, QUARTZ_STAIRS, RAILS, RED_MUSHROOM, RED_ROSE, RED_SANDSTONE_STAIRS, REDSTONE_COMPARATOR_OFF, REDSTONE_COMPARATOR_ON, REDSTONE_TORCH_OFF, REDSTONE_TORCH_ON, REDSTONE_WIRE, SANDSTONE_STAIRS, SAPLING, SEEDS, SIGN, SIGN_POST, SKULL, SMOOTH_STAIRS, SNOW, SOUL_SAND, SPRUCE_WOOD_STAIRS, STANDING_BANNER, STATIONARY_LAVA, STATIONARY_WATER, STEP, STONE_BUTTON, STONE_PLATE, STONE_SLAB2, SUGAR_CANE_BLOCK, TORCH, TRAPPED_CHEST, TRIPWIRE, TRIPWIRE_HOOK, VINE, WALL_BANNER, WALL_SIGN, WATER, WATER_LILY, WEB, WOOD_BUTTON, WOOD_PLATE, WOOD_STAIRS, WOOD_STEP, YELLOW_FLOWER);
    private static final EnumSet<Material> FULL_BLOCK = EnumSet.of(STONE, GRASS, DIRT, COBBLESTONE, WOOD, BEDROCK, SAND, GRAVEL, GOLD_ORE, IRON_ORE, COAL_ORE, LOG, LEAVES, SPONGE, GLASS, LAPIS_ORE, LAPIS_BLOCK, DISPENSER, SANDSTONE, WOOL, GOLD_BLOCK, IRON_BLOCK, DOUBLE_STEP, BRICK, TNT, BOOKSHELF, MOSSY_COBBLESTONE, OBSIDIAN, MOB_SPAWNER, DIAMOND_ORE, DIAMOND_BLOCK, WORKBENCH, FURNACE, BURNING_FURNACE, REDSTONE_ORE, GLOWING_REDSTONE_ORE, ICE, SNOW_BLOCK, CLAY, JUKEBOX, PUMPKIN, NETHERRACK, GLOWSTONE, JACK_O_LANTERN, STAINED_GLASS, SMOOTH_BRICK, HUGE_MUSHROOM_1, HUGE_MUSHROOM_2, MELON_BLOCK, MYCEL, NETHER_BRICK, ENDER_STONE, WOOD_DOUBLE_STEP, EMERALD_ORE, EMERALD_BLOCK, COMMAND, REDSTONE_BLOCK, QUARTZ_ORE, QUARTZ_BLOCK, DROPPER, STAINED_CLAY, LEAVES_2, LOG_2, BARRIER, PRISMARINE, SEA_LANTERN, HAY_BLOCK, HARD_CLAY, COAL_BLOCK, PACKED_ICE, RED_SANDSTONE, DOUBLE_STONE_SLAB2);
    private static final EnumSet<Material> TRANSPARENT = EnumSet.of(AIR, CAKE_BLOCK, REDSTONE, REDSTONE_WIRE, REDSTONE_TORCH_OFF, REDSTONE_TORCH_ON, DIODE_BLOCK_OFF, DIODE_BLOCK_ON, DETECTOR_RAIL, LEVER, STONE_BUTTON, STONE_PLATE, WOOD_PLATE, RED_MUSHROOM, BROWN_MUSHROOM, RED_ROSE, YELLOW_FLOWER, LONG_GRASS, VINE, WATER_LILY, MELON_STEM, PUMPKIN_STEM, CROPS, NETHER_WARTS, SNOW, FIRE, WEB, TRIPWIRE, TRIPWIRE_HOOK, COBBLESTONE_STAIRS, BRICK_STAIRS, SANDSTONE_STAIRS, NETHER_BRICK_STAIRS, SMOOTH_STAIRS, BIRCH_WOOD_STAIRS, WOOD_STAIRS, JUNGLE_WOOD_STAIRS, SPRUCE_WOOD_STAIRS, LAVA, STATIONARY_LAVA, WATER, STATIONARY_WATER, SAPLING, DEAD_BUSH, FENCE, FENCE_GATE, IRON_FENCE, NETHER_FENCE, LADDER, SIGN, SIGN_POST, WALL_SIGN, BED_BLOCK, BED, PISTON_EXTENSION, PISTON_MOVING_PIECE, RAILS, TORCH, TRAP_DOOR);

    public static boolean isSlabsNear(Location location) {
        Block b = location.getBlock();
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                for(int z = -1; z <= 1; z++) {
                    if (x != 0 || y != 0 || z != 0) {
                        if(SLABS.contains(b.getRelative(x, y, z).getType())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean isCloseToGround(Location location) {
        Location a = location.clone();
        a.setY(a.getY() - 0.001D);
        if (a.getBlock().getType() != AIR && a.getBlock().getType() != STRING) {
            return true;
        } else {
            a = location.clone();
            a.setY(a.getY() + 0.001D);
            return a.getBlock().getRelative(BlockFace.DOWN).getType() != AIR && a.getBlock().getType() != STRING;
        }
    }

    public static boolean isOnGround(Player player) {
        if (!PASSABLE.contains(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType())) {
            return true;
        } else {
            Location a = player.getLocation().clone();
            a.setY(a.getY() - 0.1D);
            if (!PASSABLE.contains(a.getBlock().getType())) {
                return true;
            } else {
                a = player.getLocation().clone();
                a.setY(a.getY() + 0.1D);
                return !PASSABLE.contains(a.getBlock().getRelative(BlockFace.DOWN).getType()) || isSemi(player.getLocation().getBlock().getRelative(BlockFace.DOWN));
            }
        }
    }

    public static boolean isSemi(Block b) {
        Material type = b.getType();
        return type == LADDER || type == COBBLE_WALL || type == FENCE || type == FENCE_GATE;
    }

    public static boolean isInWater(Player player) {
        Material m = player.getLocation().getBlock().getType();
        return m == Material.STATIONARY_WATER || m == Material.WATER || m == Material.STATIONARY_LAVA || m == Material.LAVA;
    }

    public static boolean isSubmersed(Player player) {
        return player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid();
    }

    public static boolean newIsInWater(Player player) {
        return player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.NORTH).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.EAST).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.WEST).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.SOUTH).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.SOUTH_WEST).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.NORTH_EAST).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.NORTH_WEST).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.SOUTH_EAST).isLiquid();
    }

    public static boolean isFullBlock(Material material) {
        return FULL_BLOCK.contains(material);
    }

    public static boolean isFacing(Location loc, Block b) {
        BlockIterator bIt = new BlockIterator(loc, 0.0D, 5);

        while(bIt.hasNext()) {
            Block c = bIt.next();
            if (c.equals(b)) {
                return true;
            }

            if (!TRANSPARENT.contains(c.getType())) {
                if ((b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) && getChestNextTo(b) == c) {
                    return true;
                }

                if (!(c.getState().getData() instanceof Door)) {
                    break;
                }

                Door d = (Door)c.getState().getData();
                if (d.isTopHalf()) {
                    d = (Door)c.getRelative(0, -1, 0).getState().getData();
                }

                if (!d.isOpen()) {
                    break;
                }
            }
        }

        return false;
    }

    public static boolean canSee(Location pLoc, Location bLoc) {
        pLoc = pLoc.clone();
        bLoc = bLoc.clone();
        Block b = bLoc.getBlock();
        for(int x = 0; x <= 1; x++) {
            for(int y = 0; y <= 1; y++) {
                for(int z = 0; z <= 1; z++) {
                    if(x == 0 && y == 0 && z == 0) {
                        if(isFacing(pLoc, b)) {
                            return true;
                        }
                    }

                    bLoc = b.getLocation().clone().add(x,y,z);
                    Location corner = lookAt(pLoc, bLoc);
                    if(isFacing(corner, b)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean canSee(Player p, Block b) {
        return canSee(p.getLocation().clone().add(0,1.62, 0), b.getLocation().clone()) ||
                canSee(p.getLocation().clone().add(0, 1.54, 0), b.getLocation().clone());
    }

    public static Location lookAt(Location loc, Location lookat) {
        loc = loc.clone();
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();
        if (dx != 0.0D) {
            if (dx < 0.0D) {
                loc.setYaw(4.712389F);
            } else {
                loc.setYaw(1.570796F);
            }

            loc.setYaw(loc.getYaw() - (float)Math.atan(dz / dx));
        } else if (dz < 0.0D) {
            loc.setYaw(3.141593F);
        }

        double dxz = Math.sqrt(Math.pow(dx, 2.0D) + Math.pow(dz, 2.0D));
        float pitch = (float)(-Math.atan(dy / dxz));
        loc.setYaw(-loc.getYaw() * 180.0F / 3.141593F + 360.0F);
        loc.setPitch(pitch * 180.0F / 3.141593F);
        return loc;
    }

    public static boolean isBlocksNear(Location loc) {
        Iterator var2 = getSurrounding(loc.getBlock(), true).iterator();

        Block block;
        while(var2.hasNext()) {
            block = (Block)var2.next();
            if (block.getType() != Material.AIR && block.getType() != Material.STATIONARY_WATER && block.getType() != Material.WATER && block.getType() != Material.LAVA && block.getType() != Material.STATIONARY_LAVA) {
                return true;
            }
        }

        var2 = getSurrounding(loc.getBlock(), false).iterator();

        while(var2.hasNext()) {
            block = (Block)var2.next();
            if (block.getType() != Material.AIR && block.getType() != Material.STATIONARY_WATER && block.getType() != Material.WATER && block.getType() != Material.LAVA && block.getType() != Material.STATIONARY_LAVA) {
                return true;
            }
        }

        loc.setY(loc.getY() - 0.5D);
        if (loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.STATIONARY_WATER && loc.getBlock().getType() != Material.WATER && loc.getBlock().getType() != Material.STATIONARY_LAVA && loc.getBlock().getType() != Material.LAVA) {
            return true;
        }

        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER})) {
            return true;
        }

        return false;
    }

    public static boolean isBlock(Block block, Material[] materials) {
        Material type = block.getType();

        for (Material m : materials) {
            if (m == type) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Block> getSurrounding(Block block, boolean diagonals) {
        ArrayList<Block> blocks = new ArrayList<>();
        if (diagonals) {
            for(int x = -1; x <= 1; ++x) {
                for(int y = -1; y <= 1; ++y) {
                    for(int z = -1; z <= 1; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        } else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }

        return blocks;
    }

    private static Block getChestNextTo(Block b) {
        Block[] c = new Block[]{b.getLocation().add(1.0D, 0.0D, 0.0D).getBlock(), b.getLocation().add(-1.0D, 0.0D, 0.0D).getBlock(), b.getLocation().add(0.0D, 0.0D, 1.0D).getBlock(), b.getLocation().add(0.0D, 0.0D, -1.0D).getBlock()};

        for (Block d : c) {
            if (d.getType() == Material.CHEST || d.getType() == Material.TRAPPED_CHEST) {
                return d;
            }
        }

        return null;
    }

    public static int getPing(Player player) {
        return ((CraftPlayer)player).getHandle().playerConnection.player.ping;
    }

    public static double getHorizontalDistance(Location one, Location two) {
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + zSqr);
        return Math.abs(sqrt);
    }

    public static double getVerticalDistance(Location one, Location two) {
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double sqrt = Math.sqrt(ySqr);
        return Math.abs(sqrt);
    }

    public static boolean isOnClimbable(Player player) {
        return isOnClimbable(player.getLocation().getBlock(), BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH)
                || isOnClimbable(player.getLocation().add(0,1,0).getBlock(), BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);
    }

    private static EnumSet<Material> upable = EnumSet.of(Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA, Material.VINE, Material.LADDER);

    public static boolean canGoUp(Location location) {
        final int startX = location.getBlockX() - 1;
        final int endX = location.getBlockX() + 1;
        final int startY = location.getBlockY() - 1;
        final int endY = location.getBlockY() + 3;
        final int startZ = location.getBlockZ() - 1;
        final int endZ = location.getBlockZ() + 1;
        for(int x = startX; x <= endX; x++) {
            for(int y = startY; y <= endY; y++) {
                for(int z = startZ; z <= endZ; z++) {
                    if(upable.contains(location.getWorld().getBlockAt(x,y,z).getType())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isOnClimbable(Block b, BlockFace... faces) {
        for(BlockFace face : faces) {
            Material type = b.getRelative(face).getType();
            if(type == Material.VINE || type == Material.LADDER) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInWeb(Player player) {
        return player.getLocation().getBlock().getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }

    public static boolean isOnIce(Player player) {
        Location a = player.getLocation();
        a.setY(a.getY() - 1.0D);
        if (!a.getBlock().getType().equals(Material.ICE) && !a.getBlock().getType().equals(Material.PACKED_ICE)) {
            a.setY(a.getY() - 1.0D);
            return a.getBlock().getType().equals(Material.ICE) || a.getBlock().getType().equals(Material.PACKED_ICE);
        } else {
            return true;
        }
    }

    public static boolean halfBlockAbove(Player player) {
        Location b = player.getEyeLocation().clone();
        b.add(0.0D, 1.0D, 0.0D);
        return b.getBlock().getType() != Material.AIR && !canStandWithin(b.getBlock());
    }

    public static boolean canStandWithin(Block block) {
        boolean isSand = block.getType() == Material.SAND;
        boolean isGravel = block.getType() == Material.GRAVEL;
        boolean solid = block.getType().isSolid() && !block.getType().name().toLowerCase().contains("door") && !block.getType().name().toLowerCase().contains("fence") && !block.getType().name().toLowerCase().contains("bars") && !block.getType().name().toLowerCase().contains("sign");
        return !isSand && !isGravel && !solid;
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static Vector getHorizontalVector(Vector v) {
        v.setY(0);
        return v;
    }

}
