package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.util.MaterialUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.TrapDoor;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class Phase extends Cheat implements Listener, Runnable {

    private BukkitTask phaseTask;
    private WeakHashMap<UUID, Integer> threshold = new WeakHashMap<>();

    public Phase(AntiCheat antiCheat) {
        super(antiCheat, CheatType.PHASE);
    }

    @Override
    public void onEnable() {
        phaseTask = antiCheat.getServer().getScheduler().runTaskTimer(antiCheat, this, 0L, 40L);
    }

    @Override
    public void onDisable() {
        phaseTask.cancel();
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (!player.getGameMode().equals(GameMode.CREATIVE) && !player.getGameMode().equals(GameMode.SPECTATOR) && !player.isOp()) {
            Location to = event.getTo();
            if (to.getY() <= 254.0D) {
                Location from = event.getFrom();
                double distance = from.distance(to);
                if (distance != 0.0D) {
                    if (from.getWorld() == to.getWorld()) {
                        if (distance > 8.0D) {
                            event.setTo(from.setDirection(to.getDirection()));
                        } else {
                            int topBlockX = from.getBlockX() < to.getBlockX() ? to.getBlockX() : from.getBlockX();
                            int bottomBlockX = from.getBlockX() > to.getBlockX() ? to.getBlockX() : from.getBlockX();
                            int topBlockY = (from.getBlockY() < to.getBlockY() ? to.getBlockY() : from.getBlockY()) + 1;
                            int bottomBlockY = from.getBlockY() > to.getBlockY() ? to.getBlockY() : from.getBlockY();
                            if (player.isInsideVehicle()) {
                                ++bottomBlockY;
                            }

                            int topBlockZ = from.getBlockZ() < to.getBlockZ() ? to.getBlockZ() : from.getBlockZ();
                            int bottomBlockZ = from.getBlockZ() > to.getBlockZ() ? to.getBlockZ() : from.getBlockZ();

                            for (int x = bottomBlockX; x <= topBlockX; ++x) {
                                for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                                    for (int y = bottomBlockY; y <= topBlockY; ++y) {
                                        Block block = from.getWorld().getBlockAt(x, y, z);
                                        if (!MaterialUtils.isPassable(block.getType()) && !MaterialUtils.isFence(block.getType()) && !MaterialUtils.isDoor(block.getType()) && !MaterialUtils.isTrapdoor(block.getType()) && !MaterialUtils.isGate(block.getType())) {
                                            if (y != bottomBlockY || from.getBlockY() == to.getBlockY()) {
                                                Location fromnew = from.clone().add(0.0D, 0.2D, 0.0D);
                                                if (MaterialUtils.isPassable(player.getLocation().getBlock().getRelative(BlockFace.UP).getType()) && MaterialUtils.isPassable(player.getLocation().getBlock().getRelative(BlockFace.UP, 1).getType()) && MaterialUtils.isPassable(player.getLocation().getBlock().getRelative(BlockFace.UP, 2).getType()) && MaterialUtils.isPassable(player.getLocation().getBlock().getRelative(BlockFace.UP, 3).getType()) && !MaterialUtils.isPassable(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType())) {
                                                    event.setTo(fromnew.setDirection(to.getDirection()));
                                                } else {
                                                    event.setTo(from.setDirection(to.getDirection()));
                                                }

                                                threshold.put(player.getUniqueId(), threshold.containsKey(player.getUniqueId()) ? threshold.get(player.getUniqueId()) + 1 : 1);
                                            }
                                        } else if (block.getType() != Material.AIR && !isValidMove(from, to)) {
                                            threshold.put(player.getUniqueId(), threshold.containsKey(player.getUniqueId()) ? threshold.get(player.getUniqueId()) + 1 : 1);
                                            event.setTo(from.setDirection(to.getDirection()));
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

    }

    private boolean isValidMove(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            return true;
        } else {
            int moveMaxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
            int moveMinX = Math.min(loc1.getBlockX(), loc2.getBlockX());
            int moveMaxY = Math.max(loc1.getBlockY(), loc2.getBlockY()) + 1;
            int moveMinY = Math.min(loc1.getBlockY(), loc2.getBlockY());
            int moveMaxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
            int moveMinZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            if (moveMaxY > 256) {
                moveMaxX = 256;
            }

            if (moveMinY > 256) {
                moveMinY = 256;
            }

            for(int x = moveMinX; x <= moveMaxX; x++) {
                for(int z = moveMinZ; z <= moveMaxZ; z++) {
                    for(int y = moveMinY; y <= moveMaxY; y++) {
                        Block block = loc1.getWorld().getBlockAt(x, y, z);
                        if ((y != moveMinY || loc1.getBlockY() == loc2.getBlockY()) && hasPhased(block, loc1, loc2)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean hasPhased(Block block, Location loc1, Location loc2) {
        if (MaterialUtils.isPassable(block.getType())) {
            return false;
        } else {
            double moveMaxX = Math.max(loc1.getX(), loc2.getX());
            double moveMinX = Math.min(loc1.getX(), loc2.getX());
            double moveMaxY = Math.max(loc1.getY(), loc2.getY()) + 1.8D;
            double moveMinY = Math.min(loc1.getY(), loc2.getY());
            double moveMaxZ = Math.max(loc1.getZ(), loc2.getZ());
            double moveMinZ = Math.min(loc1.getZ(), loc2.getZ());
            double blockMaxX = block.getLocation().getBlockX() + 1;
            double blockMinX = block.getLocation().getBlockX();
            double blockMaxY = block.getLocation().getBlockY() + 2;
            double blockMinY = block.getLocation().getBlockY();
            double blockMaxZ = block.getLocation().getBlockZ() + 1;
            double blockMinZ = block.getLocation().getBlockZ();
            if (blockMinY > moveMinY) {
                blockMaxY--;
            }

            if (MaterialUtils.isDoor(block.getType())) {
                Door var33 = (Door)block.getType().getNewData(block.getData());
                if (var33.isTopHalf()) {
                    return false;
                }

                BlockFace y = var33.getFacing();
                if (var33.isOpen()) {
                    Block up = block.getRelative(BlockFace.UP);
                    if (!MaterialUtils.isDoor(up.getType())) {
                        return false;
                    }

                    boolean z = (up.getData() & 1) == 1;
                    if (y == BlockFace.NORTH) {
                        y = z ? BlockFace.WEST : BlockFace.EAST;
                    } else if (y == BlockFace.EAST) {
                        y = z ? BlockFace.NORTH : BlockFace.SOUTH;
                    } else if (y == BlockFace.SOUTH) {
                        y = z ? BlockFace.EAST : BlockFace.WEST;
                    } else {
                        y = z ? BlockFace.SOUTH : BlockFace.NORTH;
                    }
                }

                if (y == BlockFace.WEST) {
                    blockMaxX -= 0.8D;
                }

                if (y == BlockFace.EAST) {
                    blockMinX += 0.8D;
                }

                if (y == BlockFace.NORTH) {
                    blockMaxZ -= 0.8D;
                }

                if (y == BlockFace.SOUTH) {
                    blockMinZ += 0.8D;
                }
            } else if (MaterialUtils.isGate(block.getType())) {
                if (((Gate)block.getType().getNewData(block.getData())).isOpen()) {
                    return false;
                }

                BlockFace var32 = ((Directional)block.getType().getNewData(block.getData())).getFacing();
                if (var32 != BlockFace.NORTH && var32 != BlockFace.SOUTH) {
                    blockMaxZ -= 0.2D;
                    blockMinZ += 0.2D;
                } else {
                    blockMaxX -= 0.2D;
                    blockMinX += 0.2D;
                }
            } else if (MaterialUtils.isTrapdoor(block.getType())) {
                TrapDoor x = (TrapDoor)block.getType().getNewData(block.getData());
                if (x.isOpen()) {
                    return false;
                }

                if (x.isInverted()) {
                    blockMinY += 0.85D;
                } else {
                    blockMaxY -= blockMinY > moveMinY ? 0.85D : 1.85D;
                }
            } else if (MaterialUtils.isFence(block.getType())) {
                blockMaxX -= 0.2D;
                blockMinX += 0.2D;
                blockMaxZ -= 0.2D;
                blockMinZ += 0.2D;
                if (moveMaxX > blockMaxX && moveMinX > blockMaxX && moveMaxZ > blockMaxZ && moveMinZ > blockMaxZ || moveMaxX < blockMinX && moveMinX < blockMinX && moveMaxZ > blockMaxZ && moveMinZ > blockMaxZ || moveMaxX > blockMaxX && moveMinX > blockMaxX && moveMaxZ < blockMinZ && moveMinZ < blockMinZ || moveMaxX < blockMinX && moveMinX < blockMinX && moveMaxZ < blockMinZ && moveMinZ < blockMinZ) {
                    return false;
                }

                if (block.getRelative(BlockFace.EAST).getType() == block.getType()) {
                    blockMaxX += 0.2D;
                }

                if (block.getRelative(BlockFace.WEST).getType() == block.getType()) {
                    blockMinX -= 0.2D;
                }

                if (block.getRelative(BlockFace.SOUTH).getType() == block.getType()) {
                    blockMaxZ += 0.2D;
                }

                if (block.getRelative(BlockFace.NORTH).getType() == block.getType()) {
                    blockMinZ -= 0.2D;
                }
            }

            boolean var34 = loc1.getX() < loc2.getX();
            boolean var35 = loc1.getY() < loc2.getY();
            boolean z = loc1.getZ() < loc2.getZ();
            return moveMinX != moveMaxX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ && (var34 && moveMinX <= blockMinX && moveMaxX >= blockMinX || !var34 && moveMinX <= blockMaxX && moveMaxX >= blockMaxX) || moveMinY != moveMaxY && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ && (var35 && moveMinY <= blockMinY && moveMaxY >= blockMinY || !var35 && moveMinY <= blockMaxY && moveMaxY >= blockMaxY) || moveMinZ != moveMaxZ && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && (z && moveMinZ <= blockMinZ && moveMaxZ >= blockMinZ || !z && moveMinZ <= blockMaxZ && moveMaxZ >= blockMaxZ);
        }
    }

    @Override
    public void run() {
        Iterator<Map.Entry<UUID, Integer>> iterator = threshold.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            if(entry.getValue() > 2) {
                fail(Bukkit.getPlayer(entry.getKey()), Probability.HIGH);
            }
            iterator.remove();
        }
    }
}
