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

import org.bukkit.GameMode;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerGameStateChange extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.GAME_STATE_CHANGE;
    
    public WrapperPlayServerGameStateChange() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerGameStateChange(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Enumeration of all the reason codes in Minecraft.
     * 
     * @author Kristian
     */
    public static class Reasons {
    	public static final int INVALID_BED = 0;
    	public static final int BEGIN_RAINING = 1;
    	public static final int END_RAINING = 2;
    	public static final int CHANGE_GAME_MODE = 3;
    	public static final int ENTER_CREDITS = 4;
    	
    	/**
    	 * Show demo screen. 101 - Tell movement controls, 102 - Tell jump control, 103 - Tell inventory control.
    	 */
    	public static final int DEMO_MESSAGES = 5;
    	
    	/**
    	 * Value: Appears to be played when an arrow strikes another player in Multiplayer 
    	 */
    	public static final int ARROW_HITTING_PLAYER = 6;
    	
    	/**
    	 * Value: The current darkness value. 1 = Dark, 0 = Bright.
    	 */
    	public static final int SKY_FADE_VALUE = 7;
    	
    	/**
    	 * Value: Time in ticks for the sky to fade 
    	 */
    	public static final int SKY_FADE_TIME = 8;
    	
    	private static final Reasons INSTANCE = new Reasons();
    	
    	/**
    	 * Retrieve the reasons enum.
    	 * @return Reasons enum.
    	 */
    	public static Reasons getInstance() {
    		return INSTANCE;
    	}
    }
    
    /**
     * Retrieve the reason the game state changed.
     * @see {@link Reasons}.
     * @return The current Reason
    */
    public int getReason() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the reason the game state changed.
     * @see {@link Reasons}.
     * @param value - new value.
    */
    public void setReason(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the new game mode.
     * <p>
     * Only used when reason is 3.
     * @return The current Game mode
    */
    public GameMode getGameMode() {
        return GameMode.getByValue(handle.getIntegers().read(1));
    }
    
    /**
     * Set the new game mode.
     * <p>
     * Only used when reason is 3.
     * @param value - new value.
    */
    public void setGameMode(GameMode value) {
        handle.getIntegers().write(1, value.getValue());
    }
}

