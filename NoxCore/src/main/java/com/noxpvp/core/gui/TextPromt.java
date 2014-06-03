package com.noxpvp.core.gui;

import org.bukkit.entity.Player;
import org.hamcrest.collection.IsEmptyCollection;

import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.listeners.NoxPacketListener;

public abstract class TextPromt extends NoxPacketListener {

	private CommonPacket packet;
	private Player p;
	
	public TextPromt(Player p) {
		super(PacketType.IN_UPDATE_SIGN);
		
		this.p = p;
		this.packet = new CommonPacket(PacketType.OUT_OPEN_SIGN_EDITOR);
		
	}
	
	public void show() {
		PacketUtil.sendPacket(p, packet, false);
		register();
	}
	
	@Override
	public void onPacketReceive(PacketReceiveEvent event) {
		unRegister();
		String[] ret = event.getPacket().read(PacketType.IN_UPDATE_SIGN.lines);
		
		if (new IsEmptyCollection<String>().matches(ret))
			onReturn(null);
		
		onReturn(ret);
	}
	
	public abstract void onReturn(String[] lines);

}
