package com.horizonpvp.astra.cheat.other;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class NoFall extends Cheat implements Listener {

    private WeakHashMap<UUID, Map.Entry<Long, Integer>> noFallTicks = new WeakHashMap<>();
    private WeakHashMap<UUID, Double> fallDistance = new WeakHashMap<>();

    public NoFall(AntiCheat antiCheat) {
        super(antiCheat, CheatType.NO_FALL);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(player.getAllowFlight()) {
            return;
        }

        if(player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if(player.getVehicle() != null) {
            return;
        }

        if(player.isDead() || player.getHealth() <= 0) {
            return;
        }

        if(CheckUtils.isOnClimbable(player) || CheckUtils.isInWater(player)) {
            return;
        }

        double falling = 0.0D;
        if (!CheckUtils.isOnGround(player) && e.getFrom().getY() > e.getTo().getY()) {
            if (fallDistance.containsKey(player.getUniqueId())) {
                falling = fallDistance.get(player.getUniqueId());
            }

            falling += e.getFrom().getY() - e.getTo().getY();
        }

        fallDistance.put(player.getUniqueId(), falling);
        if (falling >= 3.0D) {
            long time = System.currentTimeMillis();
            int count = 0;
            if (noFallTicks.containsKey(player.getUniqueId())) {
                time = noFallTicks.get(player.getUniqueId()).getKey();
                count = noFallTicks.get(player.getUniqueId()).getValue();
            }

            if (!player.isOnGround() && player.getFallDistance() != 0.0F) {
                count = 0;
            } else {
                count++;
            }

            if (noFallTicks.containsKey(player.getUniqueId()) && System.currentTimeMillis() - time > 10000L) {
                count = 0;
                time = System.currentTimeMillis();
            }

            if (count >= 3) {
                count = 0;
                fallDistance.put(player.getUniqueId(), 0.0D);
                fail(player, Probability.HIGH);
            }

            noFallTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(time, count));
        }
    }

}
