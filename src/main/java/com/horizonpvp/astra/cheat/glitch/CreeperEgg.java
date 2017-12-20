package com.horizonpvp.astra.cheat.glitch;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.SpawnEgg;

public class CreeperEgg extends Cheat implements Listener {

    private Material creeperEgg = new SpawnEgg(EntityType.CREEPER).getItemType();

    public CreeperEgg(AntiCheat antiCheat) {
        super(antiCheat, CheatType.CREEPER_EGG);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
        setDefault("notify", false);
    }

    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.NORMAL
    )
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK ) {
            return;
        }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if(block == null) {
            return;
        }

        if(player.getItemInHand().getType() == creeperEgg) {
            Block placedAt = block.getRelative(event.getBlockFace());
            if(placedAt.getType().isSolid() || placedAt.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType().isSolid()) {
                if(getConfig().getBoolean("block")) {
                    event.setCancelled(true);
                }

                if(getConfig().getBoolean("notify")) {
                    fail(player, Probability.LOW);
                }
            }
        }
    }

}
