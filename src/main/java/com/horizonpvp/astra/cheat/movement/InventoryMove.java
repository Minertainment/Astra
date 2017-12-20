package com.horizonpvp.astra.cheat.movement;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;
import java.util.WeakHashMap;

public class InventoryMove extends Cheat implements Listener {

    private WeakHashMap<UUID, Long> placedBlocks = new WeakHashMap<>();

    public InventoryMove(AntiCheat antiCheat) {
        super(antiCheat, CheatType.INVENTORY_MOVE);
    }

    @Override
    public void onEnable() {
        setDefault("block", true);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGHEST
    )
    private void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(event.getSlot() < 9) {
            return;
        }

        if(hasPlaced(player, 1000L)) {
            return;
        }

        if(!(player.isSprinting() || player.isSneaking() || player.isBlocking() || player.isSleeping())) {
            return;
        }

        fail(player, Probability.HIGH);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onPlace(BlockPlaceEvent e) {
        placedBlocks.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    public boolean hasPlaced(Player player, long time) {
        Long lastPlaced = placedBlocks.get(player.getUniqueId());
        return lastPlaced != null && System.currentTimeMillis() - lastPlaced <= time;
    }

}
