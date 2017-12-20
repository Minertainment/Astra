package com.horizonpvp.astra.cheat.movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.packet.WrapperPlayClientEntityAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class Sneak extends Cheat implements Listener, PacketCheat, Runnable {

    private final WeakHashMap<UUID, Integer> sps = new WeakHashMap<>();
    private final WeakHashMap<UUID, Long> blacklist = new WeakHashMap<>();
    private BukkitTask sneakTask;

    public Sneak(AntiCheat antiCheat) {
        super(antiCheat, CheatType.SNEAK_HIT, PacketType.Play.Client.ENTITY_ACTION);
    }

    @Override
    public void onEnable() {
        sneakTask = antiCheat.getServer().getScheduler().runTaskTimer(antiCheat, this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        sneakTask.cancel();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if(event.getPlayer() == null || event.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        EnumWrappers.PlayerAction playerAction = (new WrapperPlayClientEntityAction(event.getPacket())).getAction();
        if (blacklist.containsKey(event.getPlayer().getUniqueId()) && System.currentTimeMillis() - blacklist.get(event.getPlayer().getUniqueId()) < 2000L) {
            sps.put(event.getPlayer().getUniqueId(), 0);
        } else {
            if (playerAction == EnumWrappers.PlayerAction.START_SNEAKING || playerAction == EnumWrappers.PlayerAction.STOP_SNEAKING) {
                Player player = event.getPlayer();
                sps.put(player.getUniqueId(), sps.getOrDefault(player.getUniqueId(), 0) + 1);
            }

        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        if(event.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if (event.getPlayer().isSneaking() && event.getPlayer().isSprinting()) {
            fail(event.getPlayer(), Probability.DEFINITE);
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        blacklist.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @Override
    public void run() {
        Iterator<Map.Entry<UUID, Integer>> iterator = sps.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            if (entry.getValue() > 18) {
                if (System.currentTimeMillis() - blacklist.get(entry.getKey()) < 2000L) {
                    return;
                }

                fail(Bukkit.getPlayer(entry.getKey()), Probability.HIGH);
            }

            iterator.remove();
        }

    }
}
