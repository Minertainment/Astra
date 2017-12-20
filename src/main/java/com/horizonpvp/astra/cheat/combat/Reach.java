package com.horizonpvp.astra.cheat.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.util.CheckUtils;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Reach extends Cheat implements PacketCheat {

    public Reach(AntiCheat antiCheat) {
        super(antiCheat, CheatType.REACH, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(!(event.getPacket().getEntityModifier(player.getWorld()).read(0) instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = (LivingEntity)event.getPacket().getEntityModifier(player.getWorld()).read(0);
        if (target instanceof Player) {
            if (!player.getAllowFlight() && !target.isDead()) {
                double safeDistance = 4.15D;
                int ping = CheckUtils.getPing(player);
                if (ping < 80) {
                    safeDistance += 0.1D;
                } else if (ping < 160) {
                    safeDistance += 0.25D;
                } else if (ping < 300) {
                    safeDistance += 0.45D;
                } else if (ping < 440) {
                    safeDistance += 0.6D;
                } else if (ping < 500) {
                    safeDistance += 0.8D;
                } else {
                    if (ping >= 600) {
                        return;
                    }

                    safeDistance++;
                }

                for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                    if (potionEffect.getType() == PotionEffectType.SPEED) {
                        switch (potionEffect.getAmplifier()) {
                            case 0:
                                safeDistance += 0.15D;
                                break;
                            case 1:
                                safeDistance += 0.3D;
                                break;
                            case 2:
                                safeDistance += 0.45D;
                                break;
                            default:
                                return;
                        }
                    }
                }

                safeDistance += player.getVelocity().length() * 8.0D;
                if (target.getVelocity().length() > 0.08D) {
                    safeDistance += target.getVelocity().length() * 2.0D;
                }

                if (player.isSprinting()) {
                    safeDistance += 0.15D;
                }

                if (player.isBlocking()) {
                    safeDistance--;
                }

                Vector playerLoc = getEyeLocation(player);
                Vector entityLoc = target.getLocation().toVector();
                double yDiff = player.getLocation().getY() - entityLoc.getY();
                if (yDiff < 0.0D) {
                    yDiff = -yDiff;
                }

                safeDistance += yDiff / 4.0D;
                double reach = entityLoc.distance(playerLoc);
                if (reach > safeDistance) {
                    if(reach - safeDistance < 1) {
                        fail(player, Probability.HIGH);
                    } else {
                        fail(player, Probability.DEFINITE);
                    }
                }
            }
        }
    }

    private Vector getEyeLocation(Player player) {
        Location eye = player.getLocation();
        eye.setY(eye.getY() + player.getEyeHeight());
        return eye.toVector();
    }

}
