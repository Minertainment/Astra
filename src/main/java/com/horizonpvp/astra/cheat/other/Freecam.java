package com.horizonpvp.astra.cheat.other;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.util.CheckUtils;
import com.horizonpvp.astra.util.MaterialUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;
import java.util.WeakHashMap;

public class Freecam extends Cheat implements PacketCheat, Listener {

    private WeakHashMap<UUID, Long> ids = new WeakHashMap<>();

    public Freecam(AntiCheat antiCheat) {
        super(antiCheat, CheatType.FREECAM, PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
        setDefault("notifyLight", false);
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if(e.getPlayer() != null && e.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if(ids.containsKey(e.getPlayer().getUniqueId())) {
            if(System.currentTimeMillis() - ids.get(e.getPlayer().getUniqueId()) > 100 + CheckUtils.getPing(e.getPlayer())) {
                fail(e.getPlayer(), Probability.HIGH);
            }
            ids.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        ids.remove(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ids.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onTeleport(PlayerTeleportEvent e) {
        ids.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onClick(PlayerInteractEvent e) {
        if(e.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if((e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getPlayer().getItemInHand() != null) {
            if(!MaterialUtils.isOpenable(e.getClickedBlock().getType()) && e.getPlayer().getItemInHand().getType() != Material.MONSTER_EGG) {
                return;
            }


            if(!CheckUtils.canSee(e.getPlayer(), e.getClickedBlock().getRelative(e.getBlockFace()))) {
                if(getConfig().getBoolean("block")) {
                    e.setCancelled(true);
                }

                if(ids.containsKey(e.getPlayer().getUniqueId())) {
                    if(ids.get(e.getPlayer().getUniqueId()) > 100 + CheckUtils.getPing(e.getPlayer())) {
                        fail(e.getPlayer(), Probability.HIGH);
                    } else {
                        return;
                    }
                }
                ids.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void lightCreeper(PlayerInteractAtEntityEvent e) {
        if(e.getRightClicked().getType() == EntityType.CREEPER) {

        }
    }

}
