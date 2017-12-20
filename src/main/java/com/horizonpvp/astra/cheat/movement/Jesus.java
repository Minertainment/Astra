package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class Jesus extends Cheat implements Runnable, Listener {

    private HashMap<UUID, Integer> threshold = new HashMap<>();
    private BukkitTask jesusTask;

    public Jesus(AntiCheat antiCheat) {
        super(antiCheat, CheatType.JESUS);
    }

    @Override
    public void onEnable() {
        jesusTask = antiCheat.getServer().getScheduler().runTaskTimer(antiCheat, this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        jesusTask.cancel();
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(event.getFrom().getWorld() != event.getTo().getWorld()) {
            return;
        }

        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(player.isFlying() || player.getVehicle() != null || player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if(!isJesusing(player)) {
            return;
        }

        Integer previous = threshold.get(player.getUniqueId());
        threshold.put(player.getUniqueId(), previous == null ? 1 : previous + 1);
        if(threshold.get(player.getUniqueId()) >= 5) {
            fail(player, Probability.HIGH);
            event.setCancelled(true);
        }
    }

    private boolean isJesusing(Player player) {
        if (CheckUtils.isBlocksNear(player.getLocation())) {
            return false;
        } else {
            Block standing = player.getLocation().add(0, 0.175, 0).getBlock();
            Block below = player.getLocation().subtract(0, 0.175, 0).getBlock();
            return standing != null && standing.getType() == Material.AIR && below != null && (below.getType() == Material.WATER || below.getType() == Material.STATIONARY_WATER || below.getType() == Material.LAVA || below.getType() == Material.STATIONARY_LAVA);
        }
    }

    @Override
    public void run() {
        threshold.clear();
    }

}
