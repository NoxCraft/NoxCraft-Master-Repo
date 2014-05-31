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
import com.comphenix.protocol.wrappers.WrappedServerPing;

public class WrapperStatusServerOutServerInfo extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Status.Server.OUT_SERVER_INFO;
    
    public WrapperStatusServerOutServerInfo() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperStatusServerOutServerInfo(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the server information to display in the multiplayer menu.
     * @return The current ping information.
    */
    public WrappedServerPing getServerPing() {
        return handle.getServerPings().read(0);
    }
    
    /**
     * Set the server information to display in the multiplayer menu.
     * @param value - new information.
    */
    public void setServerPing(WrappedServerPing value) {
        handle.getServerPings().write(0, value);
    }
}


