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
import com.comphenix.protocol.wrappers.nbt.NbtBase;

public class WrapperPlayServerTileEntityData extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.TILE_ENTITY_DATA;
    
    public WrapperPlayServerTileEntityData() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerTileEntityData(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the x coordinate of the block associated with this tile entity.
     * @return The current X
    */
    public int getX() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the x coordinate of the block associated with this tile entity.
     * @param value - new value.
    */
    public void setX(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the y coordinate of the block associated with this tile entity.
     * @return The current Y
    */
    public short getY() {
        return handle.getIntegers().read(1).shortValue();
    }
    
    /**
     * Set the y coordinate of the block associated with this tile entity.
     * @param value - new value.
    */
    public void setY(short value) {
        handle.getIntegers().write(1, (int) value);
    }
    
    /**
     * Retrieve the z coordinate of the block associated with this tile entity.
     * @return The current Z
    */
    public int getZ() {
        return handle.getIntegers().read(2);
    }
    
    /**
     * Set the z coordinate of the block associated with this tile entity.
     * @param value - new value.
    */
    public void setZ(int value) {
        handle.getIntegers().write(2, value);
    }
    
    /**
     * Retrieve the type of update to perform.
     * @return The current Action
    */
    public byte getAction() {
        return handle.getIntegers().read(3).byteValue();
    }
    
    /**
     * Set the type of update to perform.
     * @param value - new value.
    */
    public void setAction(byte value) {
        handle.getIntegers().write(3, (int) value);
    }
    
    /**
     * Retrieve the NBT data of the current tile entity.
     * @return The current tile entity.
    */
    public NbtBase<?> getNbtData() {
        return handle.getNbtModifier().read(0);
    }
    
    /**
     * Set the NBT data of the current tile entity.
     * @param value - new value.
    */
    public void setNbtData(NbtBase<?> value) {
        handle.getNbtModifier().write(0, value);
    }
}