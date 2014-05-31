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

public class WrapperStatusClientInPing extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Status.Client.IN_PING;
    
    public WrapperStatusClientInPing() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperStatusClientInPing(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the random token we are sending.
     * @return The current random token.
    */
    public long getToken() {
        return handle.getLongs().read(0);
    }
    
    /**
     * Set the random token we are sending.
     * @param value - new token.
    */
    public void setToken(long value) {
        handle.getLongs().write(0, value);
    }   
}


