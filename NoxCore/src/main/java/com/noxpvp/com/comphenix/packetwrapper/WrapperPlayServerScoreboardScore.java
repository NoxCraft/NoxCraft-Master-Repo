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
import com.comphenix.protocol.reflect.IntEnum;

public class WrapperPlayServerScoreboardScore extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_SCORE;
        
    /**
     * Enumeration of all the known packet modes.
     * 
     * @author Kristian
     */
    public static class Modes extends IntEnum {
    	public static final int SET_SCORE = 0;
    	public static final int REMOVE_SCORE = 1;

    	private static final Modes INSTANCE = new Modes();
    	
    	public static Modes getInstance() {
    		return INSTANCE;
    	}
    }
    
    public WrapperPlayServerScoreboardScore() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerScoreboardScore(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve an unique name to be displayed in the list..
     * @return The current Item Name
    */
    public String getItemName() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set an unique name to be displayed in the list..
     * @param value - new value.
    */
    public void setItemName(String value) {
        handle.getStrings().write(0, value);
    }
    
    /**
     * Retrieve the current packet {@link Modes}.
     * <p>
     * This determines if the objective is added or removed.
     * @return The current mode.
    */
    public byte getPacketMode() {
        return handle.getIntegers().read(1).byteValue();
    }

    /**
     * Set the current packet {@link Modes}.
     * <p>
     * This determines if the objective is added or removed.
     * @param value - new value.
    */
    public void setPacketMode(byte value) {
        handle.getIntegers().write(1, (int) value);
    }
        
    /**
     * Retrieve the unique name for the scoreboard to be updated. Only sent when setting a score.
     * @return The current Score Name
    */
    public String getScoreName() {
    	return handle.getStrings().read(1);
    }
    
    /**
     * Set the unique name for the scoreboard to be updated. Only sent when setting a score.
     * @param value - new value.
    */
    public void setScoreName(String value) {
    	handle.getStrings().write(1, (String) value);
    }
    
    /**
     * Retrieve the score to be displayed next to the entry. Only sent when setting a score.
     * @return The current Value
    */
    public int getValue() {
    	return handle.getIntegers().read(0); 
    }
    
    /**
     * Set the score to be displayed next to the entry. Only sent when setting a score.
     * @param value - new value.
    */
    public void setValue(int value) {
    	handle.getIntegers().write(0, (int) value);
    }
}