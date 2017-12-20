package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.user.ACUser;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class Fly extends Cheat implements Listener {

    private Map<UUID, Map.Entry<Long, Double>> ascensionTicks = new WeakHashMap<>();
    private Map<UUID, Long> hoverTicks = new WeakHashMap<>();
    private Map<UUID, Long> flyTicks = new WeakHashMap<>();
    private Map<UUID, Integer> threshold = new WeakHashMap<>();

    public Fly(AntiCheat antiCheat) {
        super(antiCheat, CheatType.FLY);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            getUser(p).invalidate(CheatType.FLY);
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onKick(PlayerKickEvent e) {
        if(e.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if (e.getReason().contains("Flying is not enabled on this server")) {
            fail(e.getPlayer(), Probability.HIGH);
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(player.isFlying() || player.isOnGround() || player.isInsideVehicle() || player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if(isInWeb(player) && e.getTo().getWorld().getName().equals(e.getFrom().getWorld().getName())) {
            return;
        }

        ACUser user = getUser(player);
        if(System.currentTimeMillis() - 1000L <= user.getLastVelocitized()) {
            return;
        }

        if(System.currentTimeMillis() - 1000L <= user.getLastTeleported()) {
            return;
        }

        if(CheckUtils.isInWater(player)) {
           return;
        }

        Material typeBelow = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if(typeBelow != Material.WATER_LILY && typeBelow != Material.CARPET) {
            hoverCheck(player, e);
        }

        if(CheckUtils.isSubmersed(player)) {
            if(e.getTo().getY() > e.getFrom().getY()) {
                flyCheck(player, e);
            }

            return;
        }

        if(CheckUtils.isOnGround(player)) {
            return;
        }

        glideCheck(player, e);

        if(e.getTo().getY() <= e.getFrom().getY()) {
            return;
        }

        if(CheckUtils.isCloseToGround(player.getLocation())) {
            return;
        }

        if(CheckUtils.newIsInWater(player)) {
            return;
        }

        ascensionCheck(player, e);

    }

    private void ascensionCheck(Player player, PlayerMoveEvent e) {
        long time = System.currentTimeMillis();
        double totalBlocks = 0.0D;
        if (ascensionTicks.containsKey(player.getUniqueId())) {
            time = ascensionTicks.get(player.getUniqueId()).getKey();
            totalBlocks = (Double) ((Map.Entry) ascensionTicks.get(player.getUniqueId())).getValue();
        }

        long ms = System.currentTimeMillis() - time;
        double offsetY = Math.abs(e.getFrom().getY() - e.getTo().getY());
        if (offsetY > 0.0D) {
            totalBlocks += offsetY;
        }

        if (CheckUtils.isBlocksNear(player.getLocation())) {
            totalBlocks = 0.0D;
        }

        Location a = player.getLocation().subtract(0.0D, 1.0D, 0.0D);
        if (CheckUtils.isBlocksNear(a)) {
            totalBlocks = 0.0D;
        }

        double limit = 0.5D;
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.JUMP)) {
                    int level = effect.getAmplifier() + 1;
                    limit += ((double) level + 4.2D) * ((double) level + 4.2D) / 16.0D;
                    break;
                }
            }
        }

        if (ms > 550L) {
            if (totalBlocks > limit) {
                fail(player, Probability.HIGH);
                if(getConfig().getBoolean("block")) {
                    e.setTo(e.getFrom());
                }
            }

            time = System.currentTimeMillis();
        }

        ascensionTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(time, totalBlocks));
    }

    private void glideCheck(Player player, PlayerMoveEvent e) {
        if (CheckUtils.isBlocksNear(player.getLocation())) {
            if (flyTicks.containsKey(player.getUniqueId())) {
                flyTicks.remove(player.getUniqueId());
            }
        } else if (e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ()) {
            double offsetY = e.getFrom().getY() - e.getTo().getY();
            if (offsetY > 0.0D && offsetY <= 0.16D) {
                long time = System.currentTimeMillis();
                if (flyTicks.containsKey(player.getUniqueId())) {
                    time = flyTicks.get(player.getUniqueId());
                }

                long ms = System.currentTimeMillis() - time;
                if (ms > 1000L) {
                    fail(player, Probability.HIGH);
                    if(getConfig().getBoolean("block")) {
                        e.setTo(e.getFrom());
                    }
                } else {
                    flyTicks.put(player.getUniqueId(), time);
                }

            } else {
                if (flyTicks.containsKey(player.getUniqueId())) {
                    flyTicks.remove(player.getUniqueId());
                }
            }
        }
    }

    private void hoverCheck(Player player, PlayerMoveEvent e) {
        if (CheckUtils.isBlocksNear(player.getLocation())) {
            if (hoverTicks.containsKey(player.getUniqueId())) {
                hoverTicks.remove(player.getUniqueId());
            }
        } else if (e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ()) {
            if (e.getTo().getY() != e.getFrom().getY()) {
                if (hoverTicks.containsKey(player.getUniqueId())) {
                    hoverTicks.remove(player.getUniqueId());
                }
            } else {
                long time = System.currentTimeMillis();
                if (hoverTicks.containsKey(player.getUniqueId())) {
                    time = hoverTicks.get(player.getUniqueId());
                }
                long ms = System.currentTimeMillis() - time;
                if (ms > 400L) {
                    fail(player, Probability.HIGH);
                    if(getConfig().getBoolean("block")) {
                        e.setTo(e.getFrom());
                    }
                } else {
                    hoverTicks.put(player.getUniqueId(), time);
                }
            }
        }
    }

    private void flyCheck(Player player, PlayerMoveEvent e) {
        if (Math.abs(e.getFrom().getY() - e.getTo().getY()) >= 0.18D) {
            threshold.put(player.getUniqueId(), threshold.containsKey(player.getUniqueId()) ? threshold.get(player.getUniqueId()) + 1 : 1);
            if (threshold.get(player.getUniqueId()) >= 8) {
                if (e.getTo().getY() - e.getFrom().getY() > 0.0D) {
                    fail(player, Probability.HIGH);
                    if(getConfig().getBoolean("block")) {
                        e.setTo(e.getFrom());
                    }
                }

                threshold.remove(player.getUniqueId());
            }
        } else {
            if (threshold.containsKey(player.getUniqueId())) {
                threshold.put(player.getUniqueId(), threshold.get(player.getUniqueId()) > 0 ? threshold.get(player.getUniqueId()) - 1 : 0);
            }
        }
    }

    private boolean isInWeb(Player player) {
        Block b = player.getLocation().getBlock();
        return b.getType() == Material.WEB || b.getRelative(BlockFace.UP).getType() == Material.WEB || b.getRelative(BlockFace.DOWN).getType() == Material.WEB;
    }

}
