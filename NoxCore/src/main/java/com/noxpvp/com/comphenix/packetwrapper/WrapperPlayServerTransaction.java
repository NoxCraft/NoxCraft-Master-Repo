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

public class WrapperPlayServerTransaction extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.TRANSACTION;
    
    public WrapperPlayServerTransaction() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerTransaction(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the id of the window that the action occurred in.
     * @return The current Window id
    */
    public byte getWindowId() {
        return handle.getIntegers().read(0).byteValue();
    }
    
    /**
     * Set the id of the window that the action occurred in..
     * @param value - new value.
    */
    public void setWindowId(byte value) {
        handle.getIntegers().write(0, (int) value);
    }
    
    /**
     * Retrieve every action that is to be accepted has a unique number. 
     * <p>
     * This field corresponds to that number..
     * @return The current Action number
    */
    public short getActionNumber() {
        return handle.getShorts().read(0);
    }
    
    /**
     * Set every action that is to be accepted has a unique number. 
     * <p>
     * This field corresponds to that number.
     * @param value - new value.
    */
    public void setActionNumber(short value) {
        handle.getShorts().write(0, value);
    }
    
    /**
     * Retrieve whether or not the action was accepted.
     * @return The current Accepted?
    */
    public boolean getAccepted() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }
    
    /**
     * Set whether or not the action was accepted.
     * @param value - new value.
    */
    public void setAccepted(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
}
