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
import com.noxpvp.com.comphenix.packetwrapper.WrapperPlayClientClientCommand.Commands;

public class WrapperPlayClientEntityAction extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.ENTITY_ACTION;

    /**
     * Enumeration of all the entity actions.
     * @author Kristian
     */
    public static class Action extends IntEnum {
    	public static final int CROUCH = 1;
    	public static final int UNCROUCH = 2;
    	public static final int LEAVE_BED = 3;
    	public static final int START_SPRINTING = 4;
    	public static final int STOP_SPRINTING = 5;
    	
    	private static final Commands INSTANCE = new Commands();
    	
    	public static Commands getInstance() {
    		return INSTANCE;
    	}
    }
    
    public WrapperPlayClientEntityAction() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientEntityAction(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve player ID.
     * @return The current EID
    */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set player ID.
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
     * Retrieve the ID of the action.
     * @see {@link WrapperPlayClientEntityAction.Action}
     * @return The current Action ID
    */
    public byte getActionId() {
        return handle.getIntegers().read(1).byteValue();
    }
    
    /**
     * Set the ID of the action, see below.
     * @see {@link WrapperPlayClientEntityAction.Action}
     * @param value - new value.
    */
    public void setActionId(byte value) {
        handle.getIntegers().write(1, (int) value);
    }
    
    /**
     * Retrieve horse jump boost. Ranged from 0 -> 100..
     * @return The current Jump Boost
    */
    public int getJumpBoost() {
        return handle.getIntegers().read(2);
    }
    
    /**
     * Set horse jump boost. Ranged from 0 -> 100..
     * @param value - new value.
    */
    public void setJumpBoost(int value) {
        handle.getIntegers().write(2, value);
    }
}