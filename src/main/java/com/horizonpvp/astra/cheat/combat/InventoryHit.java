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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.UUID;

public class InventoryHit extends Cheat implements PacketCheat, Listener {

    private HashSet<UUID> open = new HashSet<>();

    public InventoryHit(AntiCheat antiCheat) {
        super(antiCheat, CheatType.INVENTORY_HIT, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (open.contains(player.getUniqueId())) {
            fail(player, Probability.DEFINITE);
            if(getConfig().getBoolean("block")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onInventoryOpen(InventoryOpenEvent event) {
        open.add(event.getPlayer().getUniqueId());
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onInventoryClose(InventoryCloseEvent event) {
        open.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        open.remove(event.getPlayer().getUniqueId());
    }

}
