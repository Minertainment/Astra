package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.user.ACUser;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;
import java.util.WeakHashMap;

public class Spider extends Cheat implements Listener {

    private WeakHashMap<UUID, Double> movements = new WeakHashMap<>();

    public Spider(AntiCheat antiCheat) {
        super(antiCheat, CheatType.SPIDER);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onMove(PlayerMoveEvent e) {
        if(e.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if(e.getPlayer().isFlying() || e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            movements.remove(e.getPlayer().getUniqueId());
            return;
        }

        Location to = e.getTo().clone();
        if(!(to.getBlock().getType() == Material.AIR && to.add(0,1,0).getBlock().getType() == Material.AIR && to.subtract(0,2,0).getBlock().getType() == Material.AIR)) {
            movements.remove(e.getPlayer().getUniqueId());
            return;
        }

        to = e.getFrom().clone();
        if(!(to.getBlock().getType() == Material.AIR && to.add(0,1,0).getBlock().getType() == Material.AIR && to.subtract(0,2,0).getBlock().getType() == Material.AIR)) {
            movements.remove(e.getPlayer().getUniqueId());
            return;
        }

        if(e.getTo().getY() < e.getFrom().getY() || e.getTo().getY() - e.getFrom().getY() <= 0) {
            movements.remove(e.getPlayer().getUniqueId());
            return;
        }

        ACUser user = getUser(e.getPlayer());
        if(System.currentTimeMillis() - user.getLastVelocitized() <= 1000L) {
            movements.remove(e.getPlayer().getUniqueId());
            return;
        }

        if(System.currentTimeMillis() - user.getLastTeleported() <= 1000L) {
            movements.remove(e.getPlayer().getUniqueId());
            return;
        }

        if(System.currentTimeMillis() - user.getLastFlying() <= 1000L) {
            movements.remove(e.getPlayer().getUniqueId());
            return;
        }


        if(CheckUtils.canGoUp(e.getTo().clone())) {
            movements.remove(e.getPlayer().getUniqueId());
            return;
        }


        Double value = movements.get(e.getPlayer().getUniqueId());
        if(value != null && e.getTo().getY() - e.getFrom().getY() >= value) {
            fail(e.getPlayer(), Probability.HIGH);

            if(getConfig().getBoolean("block")) {
                e.setCancelled(true);
            }
        }

        movements.put(e.getPlayer().getUniqueId(), e.getTo().getY() - e.getFrom().getY());
    }

}
