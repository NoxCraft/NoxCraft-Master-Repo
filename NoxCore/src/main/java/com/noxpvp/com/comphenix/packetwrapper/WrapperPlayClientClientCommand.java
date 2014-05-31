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

/**
 * Sent when the client is ready to complete login and when the client is ready to respawn after death.
 * @author Kristian
 */
public class WrapperPlayClientClientCommand extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.CLIENT_COMMAND;
        
    /**
     * Enumeration of all the known commands.
     * 
     * @author Kristian
     */
    public static class Commands extends IntEnum {
    	public static final int INITIAL_SPAWN = 0;
    	public static final int RESPAWN_AFTER_DEATH = 1;
    	public static final int OPEN_INVENTORY_ACHIEVEMENT = 2;
    	
    	private static final Commands INSTANCE = new Commands();
    	
    	public static Commands getInstance() {
    		return INSTANCE;
    	}
    }
    
    public WrapperPlayClientClientCommand() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientClientCommand(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve whether or not we're logging in or respawning.
     * @see {@link Commands}.
     * @return The current command
    */
    public int getCommand() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set whether or not we're logging in or respawning.
     * @see {@link Commands}.
     * @param value - new value.
    */
    public void setCommand(int value) {
        handle.getIntegers().write(0, value);
    }
}