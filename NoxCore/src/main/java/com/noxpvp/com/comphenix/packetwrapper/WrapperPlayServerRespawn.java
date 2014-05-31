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

import org.bukkit.WorldType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.Difficulty;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;

public class WrapperPlayServerRespawn extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.RESPAWN;
    
    public WrapperPlayServerRespawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerRespawn(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve -1: The Nether, 0: The Overworld, 1: The End.
     * @return The current Dimension
    */
    public int getDimension() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set -1: The Nether, 0: The Overworld, 1: The End.
     * @param value - new value.
    */
    public void setDimension(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the difficulty level.
     * @return The current Difficulty
    */
    public Difficulty getDifficulty() {
        return handle.getDifficulties().read(0);
    }
    
    /**
     * Set the difficulty level. 
     * @param value - new value.
    */
    public void setDifficulty(Difficulty value) {
        handle.getDifficulties().write(0, value);
    }
    
    /**
     * Retrieve the game mode of the current player.
     * @return The current game mode
    */
    public NativeGameMode getGameMode() {
        return handle.getGameModes().read(0);
    }
    
    /**
     * Set the game mode of the current player.
     * @param mode - new value.
    */
    public void setGameMode(NativeGameMode mode) {
        handle.getGameModes().write(0, mode);
    }
    
    /**
     * Retrieve the current level type.
     * @return The current level type
    */
    public WorldType getLevelType() {
        return handle.getWorldTypeModifier().read(0);
    }
    
    /**
     * Set see 0x01 login.
     * @param value - new world type.
    */
    public void setLevelType(WorldType value) {
        handle.getWorldTypeModifier().write(0, value);
    }
}