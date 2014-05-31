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

import javax.annotation.Nonnull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerMap extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.MAP;
    
    public WrapperPlayServerMap() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerMap(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve the damage value of the map being modified.
     * @return The current damage value.
    */
    public int getItemDamage() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the damage value of the item being modified.
     * @param value - new value.
    */
    public void setItemDamage(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve length of following byte array.
     * @return The current Text length
    */
    public byte[] getData() {
        return handle.getByteArrays().read(0);
    }
    
    /**
     * Set length of following byte array.
     * @param value - new value.
    */
    public void setData(@Nonnull byte[] value) {
    	if (value == null)
    		throw new IllegalArgumentException("Array cannot be NULL.");
        handle.getByteArrays().write(0, value);
    }
}