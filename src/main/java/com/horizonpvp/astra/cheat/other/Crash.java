package com.horizonpvp.astra.cheat.other;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class Crash extends Cheat implements Runnable, PacketCheat {

    private Map<UUID, Integer> threshold = new WeakHashMap<>();
    private Map<UUID, Integer> aps = new WeakHashMap<>();
    private BukkitTask packetTask;

    public Crash(AntiCheat antiCheat) {
        super(antiCheat, CheatType.ANIMATION_CRASH, PacketType.Play.Client.ARM_ANIMATION);
    }

    @Override
    public void onEnable() {
        packetTask = antiCheat.getServer().getScheduler().runTaskTimer(antiCheat, this, 1L, 1L);
        setDefault("max-packet-per-second", 300);
        setDefault("max-violations", 5);
        setDefault("punishment", "kick %player% Sent too many packets!");
    }

    @Override
    public void onDisable() {
        packetTask.cancel();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        Integer value = aps.get(player);
        aps.put(player, value != null ? value + 1 : 1);
    }

    @Override
    public void run() {
        for(Iterator<Map.Entry<UUID, Integer>> iterator = aps.entrySet().iterator(); iterator.hasNext(); iterator.remove()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            if (entry.getValue() > getConfig().getInt("max-packet-per-second")) {
                Integer value = threshold.get(entry.getKey());
                threshold.put(entry.getKey(), value != null ? value + 1 : 1);
                if (threshold.get(entry.getKey()) >= getConfig().getInt("max-violations")) {
                    threshold.remove(entry.getKey());
                    Player player = Bukkit.getPlayer(entry.getKey());
                    fail(player, Probability.DEFINITE);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getConfig().getString("punishment").replaceAll("%player%", player.getName()));
                }
            }
        }
    }
}
