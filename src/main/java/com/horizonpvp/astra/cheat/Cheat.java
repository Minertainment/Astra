package com.horizonpvp.astra.cheat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.type.CheatType;
import com.horizonpvp.astra.cheat.type.Probability;
import com.horizonpvp.astra.config.Config;
import com.horizonpvp.astra.user.ACUser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;

public abstract class Cheat {

    protected AntiCheat antiCheat;
    private CheatType type;
    private boolean enabled;
    private Config config;
    private PacketListener packetListener;
    private PacketType[] types;
    private HashMap<String, Object> defConfig = new HashMap<>();

    public Cheat(AntiCheat antiCheat, CheatType type) {
        this(antiCheat, type, PacketType.Play.Client.CUSTOM_PAYLOAD);
    }

    public Cheat(AntiCheat antiCheat, CheatType type, PacketType... types) {
        this.antiCheat = antiCheat;
        this.type = type;
        this.config = new Config(antiCheat, type.name.replaceAll(" ", "_"));
        this.types = types;
        defConfig.put("enabled", true);
        if(!config.exists() || getConfig().getBoolean("enabled", true)) {
            enable();
        }
    }

    public void enable() {
        enabled = true;
        onEnable();
        config.saveDefaultConfig(defConfig);
        if(this instanceof PacketCheat) {
            packetListener = new PacketAdapter(antiCheat, types) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    ((PacketCheat)Cheat.this).onPacketReceiving(event);
                }
            };

            ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);
        }

        if(this instanceof Listener) {
            antiCheat.getServer().getPluginManager().registerEvents((Listener)this, antiCheat);
        }
    }

    public void reload() {
        config.reloadConfig();
    }

    public void disable() {
        enabled = false;
        if(this instanceof PacketCheat && packetListener != null) {
            ProtocolLibrary.getProtocolManager().removePacketListener(packetListener);
        }

        if(this instanceof Listener) {
            HandlerList.unregisterAll((Listener)this);
        }

        onDisable();
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public String getName() {
        return type.name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    protected void fail(Player player, Probability prob) {
        fail(player, prob.level);
    }

    public void fail(Player player, int level) {
        getUser(player).fail(type, level);
    }

    public ACUser getUser(Player player) {
        return antiCheat.getUserManager().get(player.getUniqueId());
    }

    public void setDefault(String path, Object value) {
        defConfig.put(path, value);
    }

    public void sendDebug(String message) {
        antiCheat.getUserManager().debug(message);
    }

}
