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

import java.security.PublicKey;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperLoginServerEncryptionBegin extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Login.Server.ENCRYPTION_BEGIN;
    
    public WrapperLoginServerEncryptionBegin() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperLoginServerEncryptionBegin(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the server ID.
     * @return The current Server id
    */
    public String getServerId() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set the server ID.
     * @param value - new value.
    */
    public void setServerId(String value) {
        handle.getStrings().write(0, value);
    }
    
    /**
     * Retrieve the public key instance.
     * @return The current Public key length
    */
    public PublicKey getPublicKey() {
        return handle.getSpecificModifier(PublicKey.class).read(0);
    }
    
    /**
     * Set the public key instance.
     * @param value - new value.
    */
    public void setPublicKey(PublicKey value) {
        handle.getSpecificModifier(PublicKey.class).write(0, value);
    }
    
    /**
     * Retrieve the verify token.
     * @return The current Public key
    */
    public byte[] getVerifyToken() {
        return handle.getByteArrays().read(0);
    }
    
    /**
     * Set the verify token.
     * @param value - new value.
    */
    public void getVerifyToken(byte[] value) {
        handle.getByteArrays().write(0, value);
    }
}


