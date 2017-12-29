package com.horizonpvp.astra.cheat.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class KillAura extends Cheat implements PacketCheat {

    private HashMap<UUID, Long> attacksPerUUID = new HashMap<>();

    public KillAura(AntiCheat antiCheat) {
        super(antiCheat, CheatType.KILL_AURA, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onEnable() {
        setDefault("time-between-hits", 30L);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();
        if (player == null || player.hasPermission("anticheat.bypass")) {
            return;
        }

        if (packet.getHandle() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packetNMS = (PacketPlayInUseEntity) packet.getHandle();
            if (packetNMS.a() == null) {
                return;
            }

            int entityId = packet.getIntegers().read(0);
            Entity entity = null;

            for (Entity entityentity : player.getWorld().getEntities()) {
                if (entityentity.getEntityId() == entityId) {
                    entity = entityentity;
                }
            }

            if (packetNMS.a() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                return;
            }
            
            if (entity == null) {
                return;
            }

            if (entity instanceof Player) {
                if (attacksPerUUID.containsKey(player.getUniqueId())) {
                    Long timeLast = attacksPerUUID.get(player.getUniqueId());
                    if (System.currentTimeMillis() - timeLast < getConfig().getLong("time-between-hits")) {
                        fail(player, Probability.DEFINITE);
                        attacksPerUUID.remove(player.getUniqueId());
                        return;
                    }
                }

                attacksPerUUID.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }


    }
}
