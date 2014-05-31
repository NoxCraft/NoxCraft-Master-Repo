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

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.IntEnum;

public class WrapperPlayClientArmAnimation extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.ARM_ANIMATION;
    
    /**
     * Represents the different animations sent by notchian clients.
     * 
     * @author Kristian
     */
    public static class Animations extends IntEnum {
    	public static final int NO_ANIMATION = 0;
    	public static final int SWING_ARM = 1;
    	public static final int DAMAGE_ANIMATION = 2;
    	public static final int LEAVE_BED = 3;
    	public static final int EAT_FOOD = 5;
    	public static final int UNKNOWN = 102;
    	public static final int CROUCH = 104;
    	public static final int UNCROUCH = 105;
    	
		/**
		 * The singleton instance. Can also be retrieved from the parent class.
		 */
		private static Animations INSTANCE = new Animations();
    	
		/**
		 * Retrieve an instance of the Animation enum.
		 * @return Animation enum.
		 */
		public static Animations getInstance() {
			return INSTANCE;
		}
    }
    
    public WrapperPlayClientArmAnimation() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientArmAnimation(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the player ID.
     * @return The current EID
    */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the player ID.
     * @param value - new value.
    */
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the player's entity object.
     * @param world - the word the player has joined.
     * @return The player's entity.
     */
    public Entity getEntity(World world) {
    	return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the player's entity object.
     * @param event - the packet event.
     * @return The player's entity.
     */
    public Entity getEntity(PacketEvent event) {
    	return getEntity(event.getPlayer().getWorld());
    }
    
    /**
     * Retrieve animation ID.
     * @return The current Animation
    */
    public int getAnimation() {
        return handle.getIntegers().read(1);
    }
    
    /**
     * Set animation ID.
     * @param value - new value.
    */
    public void setAnimation(int value) {
        handle.getIntegers().write(1, value);
    }
}