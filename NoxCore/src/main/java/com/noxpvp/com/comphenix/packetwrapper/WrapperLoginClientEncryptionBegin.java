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

public class WrapperLoginClientEncryptionBegin extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Login.Client.ENCRYPTION_BEGIN;
    
    public WrapperLoginClientEncryptionBegin() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperLoginClientEncryptionBegin(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the shared secret data.
     * @return The shared data.
    */
    public byte[] getSharedSecret() {
        return handle.getByteArrays().read(0);
    }
    
    /**
     * Set the shared secret data.
     * @param value - new value.
    */
    public void getSharedSecret(byte[] value) {
        handle.getByteArrays().write(0, value);
    }
    
    /**
     * Retrieve the token response.
     * @return The shared data.
    */
    public byte[] getVerifyTokenResponse() {
        return handle.getByteArrays().read(1);
    }
    
    /**
     * Set token reponse.
     * @param value - new value.
    */
    public void setVerifyTokenResponse(byte[] value) {
        handle.getByteArrays().write(1, value);
    }
}


