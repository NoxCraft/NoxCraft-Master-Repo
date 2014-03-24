package com.noxpvp.core.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.noxpvp.core.NoxPlugin;

public class NoxPLPacketListener extends PacketAdapter {
	
	public NoxPLPacketListener(NoxPlugin plugin, PacketType... types) {
		super(plugin, types);
		
	}
	
	public void register() {
		ProtocolLibrary.getProtocolManager().addPacketListener(this);
	}
	
	public void unRegister() {
		ProtocolLibrary.getProtocolManager().removePacketListener(this);
	}

}
