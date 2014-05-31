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

import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.primitives.Ints;

public class WrapperPlayServerEntityDestroy extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_DESTROY;
    
    public WrapperPlayServerEntityDestroy() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityDestroy(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the IDs of the entities that will be destroyed.
     * @return The current entities.
    */
    public List<Integer> getEntities() {
        return Ints.asList(handle.getIntegerArrays().read(0));
    }
    
    /**
     * Set the entities that will be destroyed.
     * @param value - new value.
    */
    public void setEntities(int[] entities) {
        handle.getIntegerArrays().write(0, entities);
    }
    
    /**
     * Set the entities that will be destroyed.
     * @param value - new value.
    */
    public void setEntities(List<Integer> entities) {
        setEntities(Ints.toArray(entities));
    }
}


