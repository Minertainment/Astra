package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import java.util.UUID;
import java.util.WeakHashMap;

public class Sprint extends Cheat implements Listener {

    private final WeakHashMap<UUID, Integer> stillThreshold = new WeakHashMap<>();
    private final WeakHashMap<UUID, Integer> blockThreshold = new WeakHashMap<>();

    public Sprint(AntiCheat antiCheat) {
        super(antiCheat, CheatType.SPRINT);
    }

    @EventHandler(
            ignoreCancelled = true
    )
    private void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if (player.isFlying() || player.getAllowFlight()) {
            return;
        }

        if (!player.isSprinting()) {
            return;
        }

        if (event.getFrom() == event.getTo()) {
            stillThreshold.put(player.getUniqueId(), stillThreshold.containsKey(player.getUniqueId()) ? stillThreshold.get(player.getUniqueId()) + 1 : 1);
            if (stillThreshold.get(player.getUniqueId()) >= 10) {
                stillThreshold.remove(player.getUniqueId());
                fail(player, Probability.MODERATE);
            }
        }

        if (player.isBlocking()) {
            blockThreshold.put(player.getUniqueId(), blockThreshold.containsKey(player.getUniqueId()) ? blockThreshold.get(player.getUniqueId()) + 1 : 1);
            if (blockThreshold.get(player.getUniqueId()) >= 5) {
                blockThreshold.remove(player.getUniqueId());
                fail(player, Probability.DEFINITE);
            }
        }
    }

    @EventHandler
    private void onPlayerSprint(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if (player.isFlying() || player.getAllowFlight()) {
            return;
        }

        if (player.getFoodLevel() > 6) {
            return;
        }

        if (event.isSprinting()) {
            fail(player, Probability.DEFINITE);
        }
    }

}
