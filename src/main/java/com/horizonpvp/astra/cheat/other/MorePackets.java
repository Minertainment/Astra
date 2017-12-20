package com.horizonpvp.astra.cheat.other;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

/*
    Don't know how useful this actually is
 */
public class MorePackets extends Cheat implements Listener, PacketCheat {

    private WeakHashMap<UUID, Map.Entry<Integer, Long>> packetTicks = new WeakHashMap<>();
    private WeakHashMap<UUID, Long> lastPacket = new WeakHashMap<>();
    private ArrayList<UUID> blacklist = new ArrayList<>();

    public MorePackets(AntiCheat antiCheat) {
        super(antiCheat, CheatType.MORE_PACKETS, PacketType.Play.Client.POSITION);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }


        int count = 0;
        long time = System.currentTimeMillis();
        if (packetTicks.containsKey(player.getUniqueId())) {
            count = (Integer) ((Map.Entry) packetTicks.get(player.getUniqueId())).getKey();
            time = (Long) ((Map.Entry) packetTicks.get(player.getUniqueId())).getValue();
        }

        if (lastPacket.containsKey(player.getUniqueId())) {
            long ms = System.currentTimeMillis() - lastPacket.get(player.getUniqueId());
            if (ms >= 100L) {
                blacklist.add(player.getUniqueId());
            } else if (ms > 1L && blacklist.contains(player.getUniqueId())) {
                blacklist.remove(player.getUniqueId());
            }
        }

        if (!blacklist.contains(player.getUniqueId()) && CheckUtils.getPing(player) <= 200) {
            count++;
            if (packetTicks.containsKey(player.getUniqueId()) && System.currentTimeMillis() - time <= 1000L) {
                int maxPackets = 24;
                if (count > maxPackets) {
                    fail(player, Probability.DEFINITE);
                }

                count = 0;
                time = System.currentTimeMillis();
            }
        }

        packetTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(count, time));
        lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onWorld(PlayerChangedWorldEvent event) {
         blacklist.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        blacklist.add(event.getPlayer().getUniqueId());
    }
}
