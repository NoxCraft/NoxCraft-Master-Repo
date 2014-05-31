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

public class WrapperPlayServerScoreboardObjective extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_OBJECTIVE;
        
    /**
     * Enumeration of all the known packet modes.
     * 
     * @author Kristian
     */
    public static class Modes extends IntEnum {
    	public static final int ADD_OBJECTIVE = 0;
    	public static final int REMOVE_OBJECTIVE = 1;
    	public static final int UPDATE_VALUE = 2;

    	private static final Modes INSTANCE = new Modes();
    	
    	public static Modes getInstance() {
    		return INSTANCE;
    	}
    }
    
    public WrapperPlayServerScoreboardObjective() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerScoreboardObjective(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve an unique name for the objective.
     * @return The current Objective name
    */
    public String getObjectiveName() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set an unique name for the objective.
     * @param value - new value.
    */
    public void setObjectiveName(String value) {
        handle.getStrings().write(0, value);
    }
    
    /**
     * Retrieve the text to be displayed for the score.
     * @return The current Objective value
    */
    public String getObjectiveValue() {
        return handle.getStrings().read(1);
    }
    
    /**
     * Set the text to be displayed for the score.
     * @param value - new value.
    */
    public void setObjectiveValue(String value) {
        handle.getStrings().write(1, value);
    }
    
    /**
     * Retrieve the current packet {@link Modes}. 
     * <p>
     * This determines if the objective is added or removed.
     * @see {@link WrapperPlayServerScoreboardObjective.Modes}
     * @return The current Create/Remove
    */
    public byte getPacketMode() {
        return handle.getIntegers().read(0).byteValue();
    }
    
    /**
     * Set the current packet {@link Modes}. 
     * <p>
     * This determines if the objective is added or removed.
     * @see {@link WrapperPlayServerScoreboardObjective.Modes}
     * @param value - new value.
    */
    public void setPacketMode(byte value) {
        handle.getIntegers().write(0, (int) value);
    }
}