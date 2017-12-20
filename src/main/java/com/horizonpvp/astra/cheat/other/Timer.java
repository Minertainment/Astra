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
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;
import java.util.WeakHashMap;

public class Timer extends Cheat implements Listener, PacketCheat {

    private WeakHashMap<UUID, Long> blacklist = new WeakHashMap<>();
    private WeakHashMap<UUID, TimerPlayer> players = new WeakHashMap<>();

    public Timer(AntiCheat antiCheat) {
        super(antiCheat, CheatType.TIMER, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onMove(PlayerMoveEvent e) {
        if(e.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if(e.isCancelled()) {
            TimerPlayer tp = players.get(e.getPlayer().getUniqueId());
            if(tp != null) {
                tp.posPackets--;
            }
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if(player == null || player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (blacklist.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() - blacklist.get(player.getUniqueId()) <= 100L) {
                return;
            } else {
                blacklist.remove(player.getUniqueId());
            }
        }

        TimerPlayer tp = players.get(player.getUniqueId());
        if(tp == null) {
            tp = new TimerPlayer();
            players.put(player.getUniqueId(), tp);
        }

        tp.posPackets++;
        if (tp.posPacketTimeStart == 0) {
            tp.posPacketTimeStart = System.currentTimeMillis();
        }

        if (tp.posPacketTimeStart + 1000L <= System.currentTimeMillis()) {
            if (tp.posPackets >= 25) {
                if(tp.threshold == 0) {
                    tp.thresholdTimeStart = System.currentTimeMillis();
                }

                tp.threshold++;
                if(tp.thresholdTimeStart + 5000L <= System.currentTimeMillis()) {
                    if(tp.threshold >= 5) {
                        fail(player, Probability.HIGH);
                    } else if(tp.threshold >= 3) {
                        fail(player, Probability.LOW);
                    }
                    tp.threshold = 0;
                    tp.thresholdTimeStart = 0;
                }
            }

            tp.posPacketTimeStart = 0;
            tp.posPackets = 0;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        blacklist.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent event) {
        blacklist.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        blacklist.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    private static class TimerPlayer {

        private int posPackets;
        private long posPacketTimeStart;
        private int threshold;
        private long thresholdTimeStart;

    }

}
