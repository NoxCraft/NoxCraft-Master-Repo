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

public class WrapperPlayClientLook extends WrapperPlayClientFlying {
    public static final PacketType TYPE = PacketType.Play.Client.LOOK;
    
    public WrapperPlayClientLook() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientLook(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve absolute rotation on the X Axis, in degrees.
     * @return The current Yaw
    */
    public float getYaw() {
        return handle.getFloat().read(0);
    }
    
    /**
     * Set absolute rotation on the X Axis, in degrees.
     * @param value - new value.
    */
    public void setYaw(float value) {
        handle.getFloat().write(0, value);
    }
    
    /**
     * Retrieve absolute rotation on the Y Axis, in degrees.
     * @return The current Pitch
    */
    public float getPitch() {
        return handle.getFloat().read(1);
    }
    
    /**
     * Set absolute rotation on the Y Axis, in degrees.
     * @param value - new value.
    */
    public void setPitch(float value) {
        handle.getFloat().write(1, value);
    }
}


