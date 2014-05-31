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

import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerSetSlot extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SET_SLOT;
    
    public WrapperPlayServerSetSlot() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSetSlot(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the window which is being updated. 
     * <p>
     * Use 0 for the player inventory. This packet will only be sent for the currently opened window while the player is 
     * performing actions, even if it affects the player inventory. After the window is closed, a number of these packets
     * are sent to update the player's inventory window.
     * @return The current Window id
    */
    public byte getWindowId() {
        return handle.getIntegers().read(0).byteValue();
    }
    
    /**
     * Set the window which is being updated. 
     * <p>
     * Use 0 for the player inventory. This packet will only be sent for the currently opened window while the player is 
     * performing actions, even if it affects the player inventory. After the window is closed, a number of these packets
     * are sent to update the player's inventory window.
     * @param value - new value.
    */
    public void setWindowId(byte value) {
        handle.getIntegers().write(0, (int) value);
    }
    
    /**
     * Retrieve the index of the slot that should be changed.
     * @return The current slot
    */
    public short getSlot() {
        return handle.getIntegers().read(1).shortValue();
    }
    
    /**
     * Set the index of the slot that should be changed.
     * @param value - new value.
    */
    public void setSlot(short value) {
        handle.getIntegers().write(1, (int) value);
    }
    
    /**
     * Retrieve the new updated item stack.
     * @return The current Slot data
    */
    public ItemStack getSlotData() {
        return handle.getItemModifier().read(0);
    }
    
    /**
     * Set the new item stack.
     * @param value - new value.
    */
    public void setSlotData(ItemStack value) {
        handle.getItemModifier().write(0, value);
    }
}