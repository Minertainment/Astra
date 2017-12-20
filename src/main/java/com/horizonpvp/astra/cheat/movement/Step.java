package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.user.ACUser;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

public class Step extends Cheat implements Listener {

    public Step(AntiCheat antiCheat) {
        super(antiCheat, CheatType.STEP);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(player.isFlying() || player.hasPotionEffect(PotionEffectType.JUMP)) {
            return;
        }

        ACUser user = getUser(player);
        if(System.currentTimeMillis() - user.getLastTeleported() <= 1000L) {
            return;
        }

        if(System.currentTimeMillis() - user.getLastVelocitized() <= 1000L) {
            return;
        }

        if(!CheckUtils.isOnGround(player)) {
            return;
        }

        if(CheckUtils.isSlabsNear(player.getLocation())) {
            return;
        }

        double yDist = event.getTo().getY() - event.getFrom().getY();
        if (yDist > 0.66D) {
            if (yDist >= 1.0D) {
                player.teleport(event.getFrom(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                event.setTo(event.getFrom());
                event.setCancelled(true);
            }

            fail(player, Probability.DEFINITE);
        }
    }
}
