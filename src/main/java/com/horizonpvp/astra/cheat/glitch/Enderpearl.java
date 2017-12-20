package com.horizonpvp.astra.cheat.glitch;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Enderpearl extends Cheat implements Listener {

    public Enderpearl(AntiCheat antiCheat) {
        super(antiCheat, CheatType.ENDERPEARL);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
        setDefault("notify", false);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onPlayerInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        Material held = e.getMaterial();
        if(e.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        if (block != null && held.equals(Material.ENDER_PEARL)) {
            if(getConfig().getBoolean("block")) {
                e.setCancelled(true);
            }

            if(getConfig().getBoolean("notify")) {
                fail(e.getPlayer(), Probability.LOW);
            }
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    private void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            Location location = event.getTo();
            Block at = location.getBlock();
            Block up = at.getRelative(BlockFace.UP, 1);
            if (!isWalkable(up)) {
                if(getConfig().getBoolean("block")) {
                    event.setCancelled(true);
                }

                if(getConfig().getBoolean("notify")) {
                    fail(player, Probability.LOW);
                }
            }
        }
    }

    private boolean isWalkable(Block block) {
        return block.getType().isTransparent() || block.getType().equals(Material.STATIONARY_LAVA) || block.getType().equals(Material.STATIONARY_WATER) || block.getType().equals(Material.WATER) || block.getType().equals(Material.LAVA);
    }
}
