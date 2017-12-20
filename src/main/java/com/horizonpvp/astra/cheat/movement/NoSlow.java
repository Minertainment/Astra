package com.horizonpvp.astra.cheat.movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.Cheat;
import com.horizonpvp.astra.cheat.PacketCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class NoSlow extends Cheat implements Listener, PacketCheat, Runnable {

    private static final EnumSet<Material> DETECTABLES = EnumSet.of(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.STONE_SWORD, Material.WOOD_SWORD, Material.BOW, Material.POTION, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_FISH, Material.GRILLED_PORK, Material.PORK, Material.MUSHROOM_SOUP, Material.RAW_BEEF, Material.RAW_CHICKEN, Material.RAW_FISH, Material.APPLE, Material.GOLDEN_APPLE, Material.MELON, Material.COOKIE, Material.BREAD, Material.SPIDER_EYE, Material.ROTTEN_FLESH, Material.POTATO_ITEM);

    private final HashMap<UUID, Integer> bps = new HashMap<>();
    private BukkitTask noSlowTask;

    public NoSlow(AntiCheat antiCheat) {
        super(antiCheat, CheatType.NO_SLOW, PacketType.Play.Client.BLOCK_PLACE);
    }

    @Override
    public void onEnable() {
        noSlowTask = antiCheat.getServer().getScheduler().runTaskTimer(antiCheat, this, 0L, 5L);
    }

    @Override
    public void onDisable() {
        noSlowTask.cancel();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("anticheat.bypass")) {
            return;
        }

        if(player.getItemInHand() == null) {
            return;
        }

        if (DETECTABLES.contains(player.getItemInHand().getType())) {
            bps.put(player.getUniqueId(), bps.containsKey(player.getUniqueId()) ? bps.get(player.getUniqueId()) + 1 : 1);
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onPlace(BlockPlaceEvent event) {
        if(event.getPlayer().hasPermission("anticheat.bypass")) {
            return;
        }

        bps.put(event.getPlayer().getUniqueId(), bps.containsKey(event.getPlayer().getUniqueId()) ? bps.get(event.getPlayer().getUniqueId()) - 1 : 0);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        bps.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void run() {
        for(Iterator<Map.Entry<UUID, Integer>> iterator = bps.entrySet().iterator(); iterator.hasNext(); iterator.remove()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            if (entry.getValue() > 5) {
                fail(Bukkit.getPlayer(entry.getKey()), Probability.HIGH);
            }
        }
    }
}
