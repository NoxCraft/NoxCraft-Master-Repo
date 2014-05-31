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

public class WrapperPlayServerPlayerInfo extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.PLAYER_INFO;
    
    public WrapperPlayServerPlayerInfo() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerPlayerInfo(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the player name.
     * <p>
     * Supports chat colouring. limited to 16 characters.
     * @return The current Player name
    */
    public String getPlayerName() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set the player name.
     * <p>
     * Supports chat colouring. Limited to 16 characters.
     * @param value - new value.
    */
    public void setPlayerName(String value) {
        handle.getStrings().write(0, value);
    }
    
    /**
     * Retrieve whether or not to remove the given player from the list of online players.
     * @return The current Online
    */
    public boolean getOnline() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }
    
    /**
     * Set whether or not to remove the given player from the list of online players.
     * @param value - new value.
    */
    public void setOnline(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
    
    /**
     * Retrieve ping in milliseconds.
     * @return The current Ping
    */
    public short getPing() {
        return handle.getIntegers().read(0).shortValue();
    }
    
    /**
     * Set ping in milliseconds.
     * @param value - new value.
    */
    public void setPing(short value) {
        handle.getIntegers().write(0, (int) value);
    }
}


