package com.noxpvp.core.events.uuid;

import java.util.UUID;

import org.bukkit.event.HandlerList;

public class NoxUUIDFoundEvent extends NoxUUIDEvent {
	private static final HandlerList handlers = new HandlerList();

	public NoxUUIDFoundEvent(String username, UUID uuid) {
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
