package com.horizonpvp.astra.user;

import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.UUID;

public class ACUser {

    private EnumMap<CheatType, Integer> violations = new EnumMap<>(CheatType.class);
    private HashSet<CheatType> broadcasts = new HashSet<>();
    private long lastBroadcast;
    private boolean debug = true;
    private boolean receieveBroadcast = true;
    private boolean broadcastRequired;
    private long lastTeleported;
    private long lastVelocitized;
    private long lastFlying;
    private UUID player;
    private ACUserManager manager;

    public ACUser(ACUserManager manager, Player player) {
        this.manager = manager;
        this.player = player.getUniqueId();
    }

    public void fail(CheatType type, Probability probability) {
        fail(type, probability.level);
    }

    public void fail(CheatType type, int probability) {
        violations.putIfAbsent(type, 0);
        broadcastRequired = true;
        broadcasts.add(type);
        violations.put(type, violations.get(type) + probability);
    }

    public void invalidate(CheatType type) {
        violations.put(type, 0);
        broadcasts.remove(type);
        if(broadcasts.size() == 0) {
            broadcastRequired = false;
        }
    }

    public void invalidate(CheatType type, int probability) {
        Integer amount = violations.get(type);
        if(amount != null) {
            if(amount - probability <= 0) {
                violations.remove(type);
            } else {
                violations.put(type, amount - probability);
            }
        }

        broadcasts.remove(type);
        if(broadcasts.size() == 0) {
            broadcastRequired = false;
        }
    }

    public void invalidateAll() {
        violations.clear();
        broadcasts.clear();
        if(broadcasts.size() == 0) {
            broadcastRequired = false;
        }
    }

    public boolean isBroadcastRequired() {
        return broadcastRequired && System.currentTimeMillis() - lastBroadcast >= 5000;
    }

    public void broadcast(String format) {
        Player player = Bukkit.getPlayer(this.player);
        String name;
        if(player == null) {
            name = Bukkit.getOfflinePlayer(this.player).getName();
        } else {
            name = player.getName();
        }

        for(CheatType type : broadcasts) {
            TextComponent message = getUserBroadcast(name, format.replaceAll("%player%", name).replaceAll("%cheat%", type.name).replaceAll("%violations%", violations.get(type) + ""));
            for(ACUser user : manager.getUsers()) {
                if(user.shouldReceiveBroadcast()) {
                    user.getPlayer().spigot().sendMessage(message);
                }
            }
        }

        broadcasts.clear();
        lastBroadcast = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public void setReceieveBroadcast(boolean receieveBroadcast) {
        this.receieveBroadcast = receieveBroadcast;
    }

    public boolean shouldReceiveBroadcast() {
        Player player = getPlayer();
        return receieveBroadcast && player != null && player.isOnline() && player.hasPermission("anticheat.broadcast");
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean shouldReceieveDebug() {
        Player player = getPlayer();
        return debug && player != null && player.isOnline() && player.hasPermission("anticheat.debug");
    }

    public void setLastTeleported(long time) {
        lastTeleported = time;
    }

    public long getLastTeleported() {
        return lastTeleported;
    }

    public void setLastVelocitized(long time) {
        lastVelocitized = time;
    }

    public long getLastVelocitized() {
        return lastVelocitized;
    }

    public void setlastFlying(long time) {
        lastFlying = time;
    }

    public long getLastFlying() {
        return lastFlying;
    }

    private TextComponent getUserBroadcast(String name, String message) {
        TextComponent text = new TextComponent(message);
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + name));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {new TextComponent(ChatColor.GRAY + "Click to tp to " + name)}));
        return text;
    }
}
