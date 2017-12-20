package com.horizonpvp.astra.cheat.combat;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.user.ACUser;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class Critical extends Cheat implements Listener {

    private Map<UUID, Double> fallDistance = new WeakHashMap<>();

    public Critical(AntiCheat antiCheat) {
        super(antiCheat, CheatType.CRITICALS);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onDamage(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) {
            return;
        }

        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }

        Player damager = (Player)e.getDamager();
        if(damager.hasPermission("anticheat.bypass")) {
            return;
        }

        if(damager.isFlying() || !CheckUtils.isCloseToGround(damager.getLocation()) || fallDistance.containsKey(damager.getUniqueId())) {
            return;
        }

        if(CheckUtils.isSlabsNear(damager.getLocation())) {
            return;
        }

        ACUser user = getUser(damager);

        if(user.getLastTeleported() <= 100L) {
            return;
        }

        Location eye = damager.getLocation().clone();
        eye.add(0.0D, damager.getEyeHeight() + 1.0D, 0.0D);
        if(CheckUtils.isBlocksNear(eye)) {
            return;
        }

        double realFall = fallDistance.get(damager.getUniqueId());
        if (damager.getFallDistance() > 0.0D && !damager.isOnGround() && realFall == 0.0D) {
            fail(damager, Probability.HIGH);
        }
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

        double falling = 0.0D;
        if (!player.isOnGround() && e.getFrom().getY() > e.getTo().getY()) {
            if (fallDistance.containsKey(player.getUniqueId())) {
                falling = fallDistance.get(player.getUniqueId());
            }

            falling += e.getFrom().getY() - e.getTo().getY();
        }

        fallDistance.put(player.getUniqueId(), falling);
    }

}
