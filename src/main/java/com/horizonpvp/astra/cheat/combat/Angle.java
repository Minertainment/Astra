package com.horizonpvp.astra.cheat.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Angle extends Cheat implements PacketCheat {

    public Angle(AntiCheat antiCheat) {
        super(antiCheat, CheatType.ANGLE, PacketType.Play.Client.LOOK, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onEnable() {
        setDefault("definite-angle", 5.0D);
        setDefault("high-angle", 2.0D);
        setDefault("moderate-angle", 1.5D);
        setDefault("player-distance-squared", 9.0D);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
            if (event.getPacket().getEntityModifier(player.getWorld()).read(0) instanceof Player) {
                Player target = (Player)event.getPacket().getEntityModifier(player.getWorld()).read(0);
                if (player.getLocation().distanceSquared(target.getLocation()) >= getConfig().getDouble("player-distance-squared")) {
                    Vector perfectYaw = target.getLocation().toVector().subtract(player.getLocation().toVector());
                    Vector currentYaw = player.getLocation().getDirection();
                    float angle = perfectYaw.angle(currentYaw);
                    if (angle > getConfig().getDouble("moderate-angle", 0.1F)) {
                        fail(player, getProbabilty(angle));
                    }
                }
            }
        } else if(event.getPacketType() == PacketType.Play.Client.LOOK) {
            PacketContainer packet = event.getPacket();
            float pitch = packet.getFloat().read(1);
            if (pitch > (float)Math.abs(90)) {
                fail(player, Probability.HIGH);
            }
        }
    }

    private Probability getProbabilty(float amount) {
        if (amount > getConfig().getDouble("definite-angle")) {
            return Probability.DEFINITE;
        } else {
            return amount > getConfig().getDouble("high-angle") && amount < getConfig().getDouble("definite-angle") ? Probability.HIGH : Probability.MODERATE;
        }
    }

}
