package com.noxpvp.core.listeners;

import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.NoxPlugin;

public abstract class NoxPacketListener implements PacketListener{
	
	private PacketType[] listeningTypes;
	
	public abstract NoxPlugin getPlugin();
	
	public NoxPacketListener(PacketType... packetTypes){
		
		this.listeningTypes = packetTypes;
	}
	
	public void register(){
		PacketUtil.addPacketListener(getPlugin(), this, listeningTypes);
	}
	
	public void unRegister(){
		PacketUtil.addPacketListener(getPlugin(), this, listeningTypes);
	}
	
}
