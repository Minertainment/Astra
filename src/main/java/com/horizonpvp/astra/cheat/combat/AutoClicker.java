package com.horizonpvp.astra.cheat.combat;

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

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class AutoClicker extends Cheat implements Runnable, PacketCheat {

    private Map<UUID, Double[]> cps = new WeakHashMap<>();
    private BukkitTask task;

    public AutoClicker(AntiCheat antiCheat) {
        super(antiCheat, CheatType.AUTO_CLICKER, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onEnable() {
        task = antiCheat.getServer().getScheduler().runTaskTimer(antiCheat, this, 0L, 20L);
        setDefault("max-consistent-cps", 8.0D);
        setDefault("max-cps-high", 18.0D);
        setDefault("max-cps-definite", 22.0D);
        setDefault("max-randomized-cps", 15.0D);
    }

    @Override
    public void onDisable() {
        task.cancel();
        cps.clear();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (event.getPacket().getEntityModifier(player.getWorld()).read(0) instanceof Player) {
            Double[] values;
            if (cps.containsKey(player.getUniqueId())) {
                values = cps.get(player.getUniqueId());
                values[3] = values[3] + 1.0D;
            } else {
                values = new Double[]{0.0D, 0.0D, 0.0D, 0.0D};
            }

            cps.put(player.getUniqueId(), values);
        }
    }

    @Override
    public void run() {
        cps.forEach((uuid, doubles) -> {
            Player player = Bukkit.getPlayer(uuid);
            double mean = getMeanAverage(doubles);

            if (Arrays.stream(doubles).allMatch((clicks) -> clicks >= getConfig().getDouble("max-consistent-cps")) && doubles[3].equals(doubles[2]) && doubles[3].equals(doubles[1]) && doubles[3].equals(doubles[0])) {
                fail(player, Probability.MODERATE);
            } else if (mean >= getConfig().getDouble("max-cps-high")) {
                fail(player, Probability.HIGH);
            } else if (mean >= getConfig().getDouble("max-cps-definite")) {
                fail(player, Probability.DEFINITE);
            } else if (closelyDifferent(doubles, 2) && doubles[3] >= getConfig().getDouble("max-randomized-cps")) {
                fail(player, Probability.MODERATE);
            }

            doubles[0] = doubles[1];
            doubles[1] = doubles[2];
            doubles[2] = doubles[3];
            doubles[3] = 0.0D;
            cps.put(uuid, doubles);
        });
    }

    private double getMeanAverage(Double[] cps) {
        return (cps[3] + cps[2] + cps[1] + cps[0]) / 4.0D;
    }

    private boolean closelyDifferent(Double[] cps, int diff) {
        double a = cps[3];
        double b = cps[2];
        double c = cps[1];
        double d = cps[0];
        boolean ab = (a >= b ? a - b : b - a) <= (double)diff;
        boolean ac = (a >= c ? a - c : c - a) <= (double)diff;
        boolean ad = (a >= d ? a - d : d - a) <= (double)diff;
        return ab && ac && ad;
    }

}
