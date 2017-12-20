package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.user.ACUser;
import com.horizonpvp.astra.util.CheckUtils;
import com.horizonpvp.astra.util.MaterialUtils;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.UUID;
import java.util.WeakHashMap;

public class Knockback extends Cheat implements Listener {

    private WeakHashMap<UUID, VelocityEvent> velocitized = new WeakHashMap<>();

    public Knockback(AntiCheat antiCheat) {
        super(antiCheat, CheatType.KNOCK_BACK);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(e.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if (!velocitized.containsKey(e.getPlayer().getUniqueId())) {
            return;
        }

        VelocityEvent event = velocitized.get(e.getPlayer().getUniqueId());
        Vector movement = e.getTo().clone().subtract(e.getFrom()).toVector();
        Vector expected = event.getVelocity().divide(new Vector(2, 2, 2));

        if (MinecraftServer.currentTick - event.tick <= 10) {
            if (absAndSign(expected.getX(), movement.getX()) && absAndSign(expected.getY(), movement.getY()) && absAndSign(expected.getZ(), movement.getZ())) {
                velocitized.remove(e.getPlayer().getUniqueId());
            }
        } else {
            fail(e.getPlayer(), Probability.HIGH);
            velocitized.remove(e.getPlayer().getUniqueId());
        }
    }

    private boolean absAndSign(double expected, double move) {
        return Math.abs(expected) <= Math.abs(move) && move * expected > 0;
    }

    private HashSet<UUID> damaged = new HashSet<>();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getEntity().hasPermission("anticheat.bypass")) {
                return;
            }

            damaged.add(e.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        if(e.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if(CheckUtils.getPing(e.getPlayer()) > 300) {
            return;
        }

        if(!damaged.contains(e.getPlayer().getUniqueId())) {
            return;
        }

        damaged.remove(e.getPlayer().getUniqueId());

        if(velocitized.containsKey(e.getPlayer().getUniqueId())) {
            fail(e.getPlayer(), Probability.MODERATE);
            velocitized.remove(e.getPlayer().getUniqueId());
        }

        Vector v = e.getPlayer().getVelocity();
        AxisAlignedBB aabb = ((CraftPlayer)e.getPlayer()).getHandle().getBoundingBox().a(v.getX(), v.getY(), v.getZ());
        for(int x = (int)aabb.a; x <= (int)aabb.d; x++) {
            for(int y = (int)aabb.b; y <= (int)aabb.e; y++) {
                for(int z = (int)aabb.c; z <= (int)aabb.f; z++) {
                    Material m = e.getPlayer().getWorld().getBlockAt(x,y,z).getType();
                    if(m.isSolid() || MaterialUtils.isLiquid(m)) {
                        return;
                    }
                }
            }
        }

        velocitized.put(e.getPlayer().getUniqueId(), new VelocityEvent(e.getPlayer().getVelocity(), MinecraftServer.currentTick));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if(e.getEntity().hasPermission("anticheat.bypass")) {
            return;
        }

        ACUser user = getUser(e.getEntity());
        user.invalidate(CheatType.KNOCK_BACK, 10);
    }

    public static class VelocityEvent {

        public int tick;
        private Vector velocity;

        public VelocityEvent(Vector velocity, int tick) {
            this.tick = tick;
            this.velocity = velocity;
        }

        public Vector getVelocity() {
            return velocity.clone();
        }
    }

}
