package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.user.ACUser;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.WeakHashMap;

public class Speed extends Cheat implements Listener {

    private WeakHashMap<UUID, Integer> threshold = new WeakHashMap<>();

    public Speed(AntiCheat antiCheat) {
        super(antiCheat, CheatType.SPEED);
    }


    @EventHandler(
            ignoreCancelled = true
    )
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(player.isFlying() || player.getVehicle() != null) {
            return;
        }

        ACUser user = getUser(player);
        if(System.currentTimeMillis() - user.getLastTeleported() <= 1000L) {
            return;
        }

        if(System.currentTimeMillis() - user.getLastVelocitized() <= 2000L) {
            return;
        }

        if(System.currentTimeMillis() - user.getLastFlying() <= 1500L) {
            return;
        }

        double offsetXZ = event.getFrom().toVector().setY(0).subtract(event.getTo().toVector().setY(0)).length();

        double limitXZ;
        if (CheckUtils.isOnGround(player)) {
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                limitXZ = 0.44D;
            } else {
                limitXZ = 0.34D;
            }
        } else if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            limitXZ = 0.44D;
        } else {
            limitXZ = 0.4D;
        }

        if (CheckUtils.isSlabsNear(player.getLocation())) {
            limitXZ += 0.05D;
        }

        if (CheckUtils.isInWeb(player) || player.isBlocking() || player.isSneaking() || player.getLocation().getBlock().getType() == Material.SOUL_SAND) {
            limitXZ -= 0.1D;
        }

        if (CheckUtils.isOnIce(player)) {
            if (CheckUtils.halfBlockAbove(player)) {
                limitXZ = 1.0D;
            } else {
                limitXZ = CheckUtils.isOnGround(player) ? 0.46D : 0.66D;
            }
        } else if (CheckUtils.halfBlockAbove(player)) {
            limitXZ = 0.69D;
        }

        if (CheckUtils.isInWater(player)) {
            if (player.getInventory().getBoots() == null) {
                limitXZ -= 0.088D;
            } else if (!player.getInventory().getBoots().containsEnchantment(Enchantment.DEPTH_STRIDER)) {
                limitXZ -= 0.088D;
            }
        }

        if (offsetXZ > limitXZ) {
            threshold.put(player.getUniqueId(), threshold.containsKey(player.getUniqueId()) ? threshold.get(player.getUniqueId()) + 2 : 0);
        } else if (threshold.containsKey(player.getUniqueId()) && threshold.get(player.getUniqueId()) > 0) {
            threshold.put(player.getUniqueId(), threshold.containsKey(player.getUniqueId()) ? threshold.get(player.getUniqueId()) - 1 : 0);
        }

        if (threshold.containsKey(player.getUniqueId()) && threshold.get(player.getUniqueId()) >= 12) {
            if (offsetXZ > limitXZ) {
                fail(player, Probability.HIGH);
            }

            player.teleport(event.getFrom(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            event.setTo(event.getFrom().setDirection(event.getTo().getDirection()));
            event.setCancelled(true);

            threshold.put(player.getUniqueId(), threshold.containsKey(player.getUniqueId()) ? threshold.get(player.getUniqueId()) - 1 : 0);
        }
    }

}
