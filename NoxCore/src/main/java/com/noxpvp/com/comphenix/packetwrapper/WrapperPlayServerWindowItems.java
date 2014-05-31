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

public class WrapperPlayServerWindowItems extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.WINDOW_ITEMS;
    
    public WrapperPlayServerWindowItems() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerWindowItems(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the ID of the window which is being updated. 
     * <p>
     * Use 0 for the player inventory.
     * @return The current Window id
    */
    public byte getWindowId() {
        return handle.getIntegers().read(0).byteValue();
    }
    
    /**
     * Set the ID of the window which is being updated. 
     * <p>
     * Use 0 for the player inventory.
     * @param value - new value.
    */
    public void setWindowId(byte value) {
        handle.getIntegers().write(0, (int) value);
    }
    
    /**
     * Retrieve the items in the inventory indexed by slot index.
     * @return The items that will fill the inventory.
    */
    public ItemStack[] getItems() {
        return handle.getItemArrayModifier().read(0);
    }
    
    /**
     * Set the items in the inventory indexed by slot index.
     * @param value - new value.
    */
    public void setItems(ItemStack[] value) {
        handle.getItemArrayModifier().write(0, value);
    }
}