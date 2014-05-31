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

public class WrapperPlayServerUpdateHealth extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.UPDATE_HEALTH;
    
    public WrapperPlayServerUpdateHealth() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerUpdateHealth(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the health of the current player.
     * <p>
     * Value zero or less is dead. 20 is the full HP.
     * @return The current Health
    */
    public float getHealth() {
        return handle.getFloat().read(0);
    }
    
    /**
     * Set the health of the current player.
     * <p>
     * Value zero or less is dead. 20 is the full HP.
     * @param value - new value.
    */
    public void setHealth(float value) {
        handle.getFloat().write(0, value);
    }
    
    /**
     * Retrieve the food level.
     * <p>
     * Valid range: 0 - 20.
     * @return The current food level.
    */
    public short getFood() {
        return handle.getIntegers().read(0).shortValue();
    }
    
    /**
     * Set the food level.
     * <p>
     * Valid range: 0 - 20.
     * @param value - new value.
    */
    public void setFood(short value) {
        handle.getIntegers().write(0, (int) value);
    }
    
    /**
     * Retrieve the food saturation.
     * <p>
     * Varies from 0.0 to 5.0 in integer increments.
     * @return The current Food Saturation
    */
    public float getFoodSaturation() {
        return handle.getFloat().read(1);
    }
    
    /**
     * Set the food saturation.
     * <p>
     * Varies from 0.0 to 5.0 in integer increments.
     * @param value - new value.
    */
    public void setFoodSaturation(float value) {
        handle.getFloat().write(1, value);
    }
}