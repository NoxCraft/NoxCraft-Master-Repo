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

public class WrapperPlayServerEntityStatus extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_STATUS;
        
    public static class Status {
    	public static final int ENTITY_HURT = 2;
    	public static final int ENTITY_DEAD = 3;
    	public static final int WOLF_TAMING = 6;
    	public static final int WOLF_TAMED = 7;
    	public static final int WOLF_SHAKING_OFF_WATER = 8;
    	public static final int EATING_ACCEPTED = 9;
    	public static final int SHEEP_EATING_GRASS = 10;
    	public static final int IRON_GOLEM_GIFTING_ROSE = 11;
    	public static final int VILLAGER_SPAWN_HEART_PARTICLE = 12;
    	public static final int VILLAGER_SPAWN_ANGRY_PARTICLE = 13;
    	public static final int VILLAGER_SPAWN_HAPPY_PARTICLE = 14;
    	public static final int WITCH_SPAWN_MAGIC_PARTICLE = 15;
    	public static final int ZOMBIE_VILLAGERIZING = 16;
    	public static final int FIREWORK_EXPLODING = 17;
    	
		/**
		 * The singleton instance. Can also be retrieved from the parent class.
		 */
		private static Status INSTANCE = new Status();
    	
		/**
		 * Retrieve an instance of the status enum.
		 * @return Status enum.
		 */
		public static Status getInstance() {
			return INSTANCE;
		}
    }
    
    public WrapperPlayServerEntityStatus() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityStatus(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the entity ID.
     * @return The current Entity ID
    */
    public int getEntityId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Retrieve the entity.
     * @param world - the current world of the entity.
     * @return The entity.
     */
    public Entity getEntity(World world) {
    	return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity.
     * @param event - the packet event.
     * @return The entity.
     */
    public Entity getEntity(PacketEvent event) {
    	return getEntity(event.getPlayer().getWorld());
    }
    
    /**
     * Set the entity ID.
     * @param value - new value.
    */
    public void setEntityId(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve entity status. See {@link Status}.
     * @return The current Entity Status
    */
    public int getEntityStatus() {
        return handle.getBytes().read(0).intValue();
    }
    
    /**
     * Set the entity status. See {@link Status}.
     * @param value - new value.
    */
    public void setEntityStatus(int value) {
        handle.getBytes().write(0, (byte) value);
    }
}