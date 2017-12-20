package com.horizonpvp.astra.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayClientEntityAction extends AbstractPacket {

    public static final PacketType TYPE;

    public WrapperPlayClientEntityAction() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientEntityAction(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }

    public EnumWrappers.PlayerAction getAction() {
        return handle.getPlayerActions().read(0);
    }

    public void setAction(EnumWrappers.PlayerAction value) {
        handle.getPlayerActions().write(0, value);
    }

    public int getJumpBoost() {
        return handle.getIntegers().read(1);
    }

    public void setJumpBoost(int value) {
        handle.getIntegers().write(1, value);
    }

    static {
        TYPE = PacketType.Play.Client.ENTITY_ACTION;
    }
}
