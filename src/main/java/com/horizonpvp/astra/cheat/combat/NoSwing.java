package com.horizonpvp.astra.cheat.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;
import java.util.WeakHashMap;

public class NoSwing extends Cheat implements PacketCheat, Listener {

    private WeakHashMap<UUID, Long> lastArmSwing = new WeakHashMap<>();

    public NoSwing(AntiCheat antiCheat) {
        super(antiCheat, CheatType.NO_SWING, PacketType.Play.Client.ARM_ANIMATION);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        lastArmSwing.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }

        final Player damager = (Player)e.getDamager();
        if(damager.hasPermission("anticheat.bypass")) {
            return;
        }

        antiCheat.getServer().getScheduler().runTaskLater(antiCheat, () -> {
            if(!hasSwung(damager, 1500L)) {
                fail(damager, Probability.HIGH);
            }
        }, 10L);
    }

    private boolean hasSwung(Player player, Long time) {
        return lastArmSwing.containsKey(player.getUniqueId()) && System.currentTimeMillis() < lastArmSwing.get(player.getUniqueId()) + time;
    }

}
