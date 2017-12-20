package com.horizonpvp.astra.cheat.other;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.UUID;
import java.util.WeakHashMap;

public class Regen extends Cheat implements Listener {

    private WeakHashMap<UUID, Long> lastNaturalRegen = new WeakHashMap<>();

    public Regen(AntiCheat antiCheat) {
        super(antiCheat, CheatType.REGEN);
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onNaturalRegen(EntityRegainHealthEvent event) {
        if(event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED) {
            return;
        }

        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = ((Player) event.getEntity()).getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        long time = System.currentTimeMillis();
        if (lastNaturalRegen.containsKey(player.getUniqueId()) && time - lastNaturalRegen.get(player.getUniqueId()) < 2000L) {
            event.setCancelled(true);
            fail(player, Probability.DEFINITE);
        } else {
            lastNaturalRegen.put(player.getUniqueId(), time);
        }
    }
}
