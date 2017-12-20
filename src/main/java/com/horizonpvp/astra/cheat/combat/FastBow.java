package com.horizonpvp.astra.cheat.combat;

import com.comphenix.protocol.PacketType;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.UUID;
import java.util.WeakHashMap;

public class FastBow extends Cheat implements Listener {

    private WeakHashMap<UUID, Long> shots = new WeakHashMap<>();

    public FastBow(AntiCheat antiCheat) {
        super(antiCheat, CheatType.FAST_BOW, PacketType.Play.Client.CUSTOM_PAYLOAD);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onEntityShootBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && e.getForce() >= 1.0F) {
            Player player = (Player) e.getEntity();
            if(player.hasPermission("anticheat.bypass")) {
                return;
            }

            if (shots.containsKey(player.getUniqueId()) && shots.get(player.getUniqueId()) > System.currentTimeMillis()) {
                e.setCancelled(true);
                fail(player, Probability.DEFINITE);
            } else {
                shots.put(player.getUniqueId(), System.currentTimeMillis() + 1000L);
            }
        }
    }

}
