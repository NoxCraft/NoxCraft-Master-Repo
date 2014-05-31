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

public class WrapperPlayClientWindowClick extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.WINDOW_CLICK;
    
    public WrapperPlayClientWindowClick() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientWindowClick(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the id of the window which was clicked. 
     * <p>
     * Use 0 for the player inventory.
     * @return The current Window id
    */
    public byte getWindowId() {
        return handle.getIntegers().read(0).byteValue();
    }
    
    /**
     * Set the id of the window which was clicked. 
     * <p>
     * Use 0 for the player inventory.
     * @param value - new value.
    */
    public void setWindowId(byte value) {
        handle.getIntegers().write(0, (int) value);
    }
    
    /**
     * Retrieve the clicked slot index.
     * @return The current Slot
    */
    public short getSlot() {
        return handle.getIntegers().read(1).shortValue();
    }
    
    /**
     * Set the clicked slot index.
     * @param value - new value.
    */
    public void setSlot(short value) {
        handle.getIntegers().write(1, (int) value);
    }
    
    /**
     * Retrieve the mouse button that was clicked.
     * <p>
     * Here zero is left click, one is right click and three is middle click.
     * @return The current Mouse button
    */
    public byte getMouseButton() {
        return handle.getIntegers().read(2).byteValue();
    }
    
    /**
     * Set the mouse button that was clicked.
     * <p>
     * Here zero is left click, one is right click and three is middle click.
     * @param value - new value.
    */
    public void setMouseButton(byte value) {
        handle.getIntegers().write(2, (int) value);
    }
    
    /**
     * Retrieve a unique number for the action, used for transaction handling (See the Transaction packet)..
     * @return The current Action number
    */
    public short getActionNumber() {
        return handle.getShorts().read(0);
    }
    
    /**
     * Set a unique number for the action, used for transaction handling (See the Transaction packet)..
     * @param value - new value.
    */
    public void setActionNumber(short value) {
        handle.getShorts().write(0, value);
    }
    
    /**
     * Retrieve the click mode.
     * <p>
     * See <a href="http://wiki.vg/Protocol#Click_Window">Click Window</a> for more details.
     * @return The current mode.
    */
    public int getMode() {
        return handle.getIntegers().read(3);
    }
    
    /**
     * Set the click mode.
     * <p>
     * See <a href="http://wiki.vg/Protocol#Click_Window">Click Window</a> for more details.
     * @param value - new value.
    */
    public void setMode(int mode) {
        handle.getIntegers().write(3, mode);
    }
    
    /**
     * Retrieve the item that was clicked in the inventory.
     * @return The current Clicked item
    */
    public ItemStack getClickedItem() {
        return handle.getItemModifier().read(0);
    }
    
    /**
     * Set the item that was clicked in the inventory.
     * @param value - new value.
    */
    public void setClickedItem(ItemStack value) {
        handle.getItemModifier().write(0, value);
    }
}