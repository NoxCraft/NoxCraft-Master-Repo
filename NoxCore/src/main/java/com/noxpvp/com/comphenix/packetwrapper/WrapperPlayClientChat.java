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

public class WrapperPlayClientChat extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.CHAT;
    
    public WrapperPlayClientChat() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientChat(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the message sent by this player.
     * @return The current Message
    */
    public String getMessage() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set the message sent by this player.
     * @param value - new value.
    */
    public void setMessage(String value) {
        handle.getStrings().write(0, value);
    }
}


