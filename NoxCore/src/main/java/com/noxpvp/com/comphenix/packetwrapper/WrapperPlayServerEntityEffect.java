/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.com.comphenix.packetwrapper;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class WrapperPlayServerEntityEffect extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_EFFECT;
    
    public WrapperPlayServerEntityEffect() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityEffect(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve entity ID of a player.
     * @return The current Entity ID
    */
    public int getEntityId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set entity ID of a player.
     * @param value - new value.
    */
    public void setEntityId(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the entity.
     * @param world - the current world of the entity.
     * @return The entity.
     */
    public Entity getEntity(World world) {
    	return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity.
     * @param event - the packet event.
     * @return The entity.
     */
    public Entity getEntity(PacketEvent event) {
    	return getEntity(event.getPlayer().getWorld());
    }
    
    /**
     * Retrieve the effect ID.
     * @return The current effect ID
    */
    public byte getEffectId() {
        return handle.getBytes().read(0);
    }
    
    /**
     * Set the effect id.
     * @param value - new value.
    */
    public void setEffectId(byte value) {
        handle.getBytes().write(0, value);
    }
    
    /**
     * Retrieve the effect.
     * @return The current effect
    */
    public PotionEffectType getEffect() {
        return PotionEffectType.getById(getEffectId());
    }
    
    /**
     * Set the effect id.
     * @param value - new value.
    */
    public void setEffect(PotionEffectType value) {
        setEffectId((byte) value.getId());
    }
    
    /**
     * Retrieve the amplifier.
     * @return The current Amplifier
    */
    public byte getAmplifier() {
        return handle.getBytes().read(1);
    }
    
    /**
     * Set the amplifier.
     * @param value - new value.
    */
    public void setAmplifier(byte value) {
        handle.getBytes().write(1, value);
    }
    
    /**
     * Retrieve duration in ticks.
     * @return The current Duration
    */
    public short getDuration() {
        return handle.getShorts().read(0);
    }
    
    /**
     * Set the duration in ticks.
     * @param value - new value.
    */
    public void setDuration(short value) {
        handle.getShorts().write(0, value);
    }
}