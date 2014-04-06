package com.noxpvp.core.events.uuid;

import java.util.UUID;

import org.bukkit.event.HandlerList;

public class NoxUUIDLostEvent extends NoxUUIDEvent {
	public NoxUUIDLostEvent(String username, UUID uuid) {
		super(username, uuid);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
