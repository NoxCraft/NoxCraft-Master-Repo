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

public class WrapperPlayClientFlying extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.FLYING;
    
    public WrapperPlayClientFlying() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientFlying(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    protected WrapperPlayClientFlying(PacketContainer packet, PacketType type) {
        super(packet, type);
    }
    
    /**
     * Retrieve the ground state.
     * <p>
     * True if the client is on the ground, false otherwise.
     * @return The current On Ground
    */
    public boolean getOnGround() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }
    
    /**
     * Set the ground state.
     * <p>
     * True if the client is on the ground, false otherwise.
     * @param value - new value.
    */
    public void setOnGround(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
}