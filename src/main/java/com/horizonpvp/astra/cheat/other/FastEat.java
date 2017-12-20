package com.horizonpvp.astra.cheat.other;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class FastEat extends Cheat implements PacketCheat, Listener {

    private final Map<UUID, Long> startEat = new WeakHashMap<>();

    public FastEat(AntiCheat antiCheat) {
        super(antiCheat, CheatType.FAST_EAT, PacketType.Play.Client.BLOCK_PLACE);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType().isEdible()) {
            startEat.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onFoodLevel(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            if (event.getFoodLevel() >= player.getFoodLevel()) {
                if (startEat.containsKey(player.getUniqueId()) && System.currentTimeMillis() - startEat.get(player.getUniqueId()) < 1000L) {
                    fail(player, Probability.MODERATE);
                }

            }
        }
    }

}
