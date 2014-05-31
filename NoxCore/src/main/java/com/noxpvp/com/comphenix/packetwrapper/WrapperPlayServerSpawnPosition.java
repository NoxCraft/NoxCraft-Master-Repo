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

import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerSpawnPosition extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_POSITION;
    
    public WrapperPlayServerSpawnPosition() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnPosition(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the X coordinate of the spawn point.
     * @return The current x coordinate.
    */
    public int getX() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the X coordinate of the spawn point.
     * @param value - new value.
    */
    public void setX(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the Y coordinate of the spawn point.
     * @return The current Y
    */
    public int getY() {
        return handle.getIntegers().read(1);
    }
    
    /**
     * Set the Y coordinate of the spawn point.
     * @param value - new value.
    */
    public void setY(int value) {
        handle.getIntegers().write(1, value);
    }
    
    /**
     * Retrieve the Z coordinate of the spawn point.
     * @return The current Z
    */
    public int getZ() {
        return handle.getIntegers().read(2);
    }
    
    /**
     * Set the Z coordinate of the spawn point..
     * @param value - new value.
    */
    public void setZ(int value) {
        handle.getIntegers().write(2, value);
    }
    
    /**
     * Set the spawn location using a vector.
     * @param point - the new spawn location.
     */
    public void setLocation(Vector point) {
    	setX(point.getBlockX());
    	setY(point.getBlockY());
    	setZ(point.getBlockZ());
    }
    
    /**
     * Retrieve the spawn location as a vector.
     * @return The spawn location.
     */
    public Vector getLocation() {
    	return new Vector(getX(), getY(), getZ());
    }
}