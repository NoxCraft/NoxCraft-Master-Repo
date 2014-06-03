package com.noxpvp.core.gui;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.listeners.NoxPacketListener;

public abstract class TextPrompt extends NoxPacketListener {

	private CommonPacket packet;
	private Player p;
	
	public TextPrompt(Player p) {
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
		
		if (LogicUtil.nullOrEmpty(ret))
			onReturn(null);
		
		onReturn(ret);
	}
	
	public abstract void onReturn(String[] lines);

}
