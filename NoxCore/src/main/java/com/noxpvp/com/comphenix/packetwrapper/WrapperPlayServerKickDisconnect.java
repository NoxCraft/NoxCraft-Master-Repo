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
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperPlayServerKickDisconnect extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.KICK_DISCONNECT;
    
    public WrapperPlayServerKickDisconnect() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerKickDisconnect(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the reason that is displayed to the client when the connection terminates. 
     * @return The current Reason
    */
    public WrappedChatComponent getReason() {
        return handle.getChatComponents().read(0);
    }
    
    /**
     * Set the reason that is displayed to the client when the connection terminates.
     * @param value - new reason.
    */
    public void setReason(WrappedChatComponent value) {
        handle.getChatComponents().write(0, value);
    }   
}