package com.noxpvp.core.events.uuid;

import java.util.UUID;

import org.bukkit.event.HandlerList;

public class NoxUUIDLostEvent extends NoxUUIDEvent {
	private static final HandlerList handlers = new HandlerList();

	public NoxUUIDLostEvent(String username, UUID uuid) {
		super(username, uuid);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
