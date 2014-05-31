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

public class WrapperPlayClientEnchantItem extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.ENCHANT_ITEM;
    
    public WrapperPlayClientEnchantItem() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientEnchantItem(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the ID sent by open window.
     * @return The current Window ID
    */
    public byte getWindowId() {
        return handle.getIntegers().read(0).byteValue();
    }
    
    /**
     * Set the ID sent by open window.
     * @param value - new value.
    */
    public void setWindowId(byte value) {
        handle.getIntegers().write(0, (int) value);
    }
    
    /**
     * Retrieve the position of the enchantment on the enchantment table window, starting with 0 as the top-most one.
     * @return The current Enchantment
    */
    public byte getEnchantment() {
        return handle.getIntegers().read(1).byteValue();
    }
    
    /**
     * Set the position of the enchantment on the enchantment table window, starting with 0 as the top-most one.
     * @param value - new value.
    */
    public void setEnchantment(byte value) {
        handle.getIntegers().write(1, (int) value);
    }
}