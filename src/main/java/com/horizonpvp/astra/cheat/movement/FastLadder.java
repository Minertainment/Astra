package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.user.ACUser;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class FastLadder extends Cheat implements Listener {

    private Map<UUID, Integer> threshold = new WeakHashMap<>();

    public FastLadder(AntiCheat antiCheat) {
        super(antiCheat, CheatType.FAST_LADDER);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(player.isFlying() || player.hasPotionEffect(PotionEffectType.JUMP)) {
            return;
        }

        ACUser user = getUser(player);
        if(user.getLastVelocitized() + 1000L <= System.currentTimeMillis()) {
            return;
        }

        if(CheckUtils.isSlabsNear(player.getLocation())) {
            return;
        }

        if(!CheckUtils.isOnClimbable(player)) {
            return;
        }


        if (event.getTo().getY() - event.getFrom().getY() >= 0.25D && event.getTo().getY() - event.getFrom().getY() != 0.41999998688697815D && event.getTo().getY() - event.getFrom().getY() != 0.33319999363422426D && event.getTo().getY() - event.getFrom().getY() != 0.24813599859093927D && event.getTo().getY() - event.getFrom().getY() != 0.1647732818260721D && event.getTo().getY() - event.getFrom().getY() != 0.1544480052490229D && event.getTo().getY() - event.getFrom().getY() != 0.40514633092241326D) {
            threshold.put(player.getUniqueId(), threshold.containsKey(player.getUniqueId()) ? threshold.get(player.getUniqueId()) + 1 : 1);
            if (threshold.get(player.getUniqueId()) >= 3) {
                fail(player, Probability.HIGH);
            }
        }
    }

}
