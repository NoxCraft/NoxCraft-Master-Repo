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

public class WrapperPlayServerExperience extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.EXPERIENCE;
    
    public WrapperPlayServerExperience() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerExperience(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the new amount of experience in the experience bar as a value between 0 and 1.
     * @return The current Experience bar
    */
    public float getExperienceBar() {
        return handle.getFloat().read(0);
    }
    
    /**
     * Set the new amount of experience in the experience bar as a value between 0 and 1.
     * @param value - new value.
    */
    public void setExperienceBar(float value) {
        handle.getFloat().write(0, value);
    }
    
    /**
     * Retrieve the displayed level.
     * @return The current Level
    */
    public short getLevel() {
        return handle.getIntegers().read(1).shortValue();
    }
    
    /**
     * Set the displayed level.
     * @param value - new value.
    */
    public void setLevel(short value) {
        handle.getIntegers().write(1, (int) value);
    }
    
    /**
     * Retrieve the total amount of experienced gained.
     * @return The current Total experience
    */
    public short getTotalExperience() {
        return handle.getIntegers().read(0).shortValue();
    }
    
    /**
     * Set the total amount of experience gained.
     * @param value - new value.
    */
    public void setTotalExperience(short value) {
        handle.getIntegers().write(0, (int) value);
    }
}


