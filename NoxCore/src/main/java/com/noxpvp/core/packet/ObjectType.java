package com.noxpvp.core.packet;

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

}
