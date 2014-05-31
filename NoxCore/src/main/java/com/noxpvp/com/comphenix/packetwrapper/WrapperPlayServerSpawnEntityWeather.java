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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerSpawnEntityWeather extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY_WEATHER;
    
    public WrapperPlayServerSpawnEntityWeather() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnEntityWeather(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the entity ID of the thunderbolt.
     * @return The current Entity ID
    */
    public int getEntityId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the entity ID of the thunderbolt.
     * @param value - new value.
    */
    public void setEntityId(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the global entity type.
     * <p>
     * Currently always 1 for thunderbolt.
     * @return The current Type
    */
    public byte getType() {
        return handle.getIntegers().read(4).byteValue();
    }
    
    /**
     * Set the global entity type.
     * <p>
     * Currently always 1 for thunderbolt.
     * @param value - new value.
    */
    public void setType(byte value) {
        handle.getIntegers().write(4, (int) value);
    }
    
    /**
     * Retrieve the x coordinate of the thunderbolt.
     * @return The current X
    */
    public double getX() {
        return handle.getIntegers().read(1) / 32.0D;
    }
    
    /**
     * Set the x coordinate of the thunderbolt.
     * @param value - new value.
    */
    public void setX(double value) {
        handle.getIntegers().write(1, (int) (value * 32.0D));
    }
    
    /**
     * Retrieve the y coordinate of the thunderbolt.
     * @return The current y
    */
    public double getY() {
        return handle.getIntegers().read(2) / 32.0D;
    }
    
    /**
     * Set the y coordinate of the thunderbolt.
     * @param value - new value.
    */
    public void setY(double value) {
        handle.getIntegers().write(2, (int) (value * 32.0D));
    }
    
    /**
     * Retrieve the z coordinate of the thunderbolt.
     * @return The current z
    */
    public double getZ() {
        return handle.getIntegers().read(3) / 32.0D;
    }
    
    /**
     * Set the z coordinate of the thunderbolt.
     * @param value - new value.
    */
    public void setZ(double value) {
        handle.getIntegers().write(3, (int) (value * 32.0D));
    }
}