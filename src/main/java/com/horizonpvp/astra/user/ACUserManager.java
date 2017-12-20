package com.horizonpvp.astra.user;

import com.horizonpvp.astra.AntiCheat;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ACUserManager implements Listener, Runnable {

    private AntiCheat anitCheat;
    private BukkitTask broadcastTask;
    private HashMap<UUID, ACUser> users = new HashMap<>();

    public ACUserManager(AntiCheat antiCheat) {
        this.anitCheat = antiCheat;
        antiCheat.getServer().getPluginManager().registerEvents(this, antiCheat);
        broadcastTask = antiCheat.getServer().getScheduler().runTaskTimer(antiCheat, this, 0L, 1L);
    }

    public void broadcast() {
        for(ACUser user : users.values()) {
            if(user.isBroadcastRequired()) {
                user.broadcast(ChatColor.translateAlternateColorCodes('&', anitCheat.getConfig().getString("alert-message")));
            }
        }
    }

    public void debug(String message) {
        for(ACUser user : users.values()) {
            if(user.shouldReceieveDebug()) {
                user.getPlayer().sendMessage(message);
            }
        }
    }

    @Override
    public void run() {
        broadcast();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(ChatColor.RED + " " + ChatColor.RED + " " + ChatColor.YELLOW + " " + ChatColor.AQUA);
        ACUser user = users.putIfAbsent(e.getPlayer().getUniqueId(), new ACUser(this, e.getPlayer()));
        if(user != null) {
            user.invalidateAll();
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onTeleport(PlayerTeleportEvent e) {
        if(e.getFrom().getWorld() == e.getTo().getWorld() && e.getFrom().distance(e.getTo()) <= 20) {
            return;
        }

        ACUser user = users.get(e.getPlayer().getUniqueId());
        user.invalidateAll();
        user.setLastTeleported(System.currentTimeMillis());
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        if(!e.isFlying()) {
            users.get(e.getPlayer().getUniqueId()).setlastFlying(System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        users.get(e.getPlayer().getUniqueId()).invalidateAll();
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        users.get(e.getPlayer().getUniqueId()).setLastVelocitized(System.currentTimeMillis());
    }

    public ACUser get(UUID id) {
        return users.get(id);
    }

    public Collection<ACUser> getUsers() {
        return users.values();
    }

}
