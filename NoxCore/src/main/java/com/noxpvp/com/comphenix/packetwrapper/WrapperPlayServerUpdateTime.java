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

public class WrapperPlayServerUpdateTime extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.UPDATE_TIME;
    
    public WrapperPlayServerUpdateTime() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerUpdateTime(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the age of the world in ticks. 
     * <p>
     * This cannot be changed by server commands.
     * @return The current age of the world
    */
    public long getAgeOfTheWorld() {
        return handle.getLongs().read(0);
    }
    
    /**
     * Set the age of the world in ticks.
     * <p>
     * This cannot be changed by server commands.
     * @param value - new value.
    */
    public void setAgeOfTheWorld(long value) {
        handle.getLongs().write(0, value);
    }
    
    /**
     * Retrieve the world (or region) time, in ticks.
     * @return The current Time of Day
    */
    public long getTimeOfDay() {
        return handle.getLongs().read(1);
    }
    
    /**
     * Set the world (or region) time, in ticks.
     * @param value - new value.
    */
    public void setTimeOfDay(long value) {
        handle.getLongs().write(1, value);
    }
}