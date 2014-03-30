package com.noxpvp.core.packet;

import org.bukkit.entity.EntityType;

public enum ObjectType {
	
	ITEM_STACK((byte) 2),
	ARROW((byte) 60),
	FISHING_FLOAT((byte) 90);
	
	private byte id;
	
	ObjectType(byte id) {
		this.id = id;
	}
	
	public byte getByteId() {
		return this.id;
	}
	
	public static byte getByteIdByType(EntityType type) {
		switch (type) {
		case ARROW:
			return 60;
		case BOAT:
			return 1;
		case DROPPED_ITEM: 
			return 2;
		default:
			return 0;
		}//TODO finish adding these
		
	}

}
