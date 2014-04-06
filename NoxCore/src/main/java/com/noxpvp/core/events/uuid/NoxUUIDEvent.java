package com.noxpvp.core.events.uuid;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NoxUUIDEvent extends Event {
	
	private final String username;
	private final UUID uuid;

	public NoxUUIDEvent(String username, UUID uuid) {
		super(!Bukkit.isPrimaryThread());
		this.username = username;
		this.uuid = uuid;
	}
	
	public String getUsername() {
		return username;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public UUID getUUID() {
		return uuid;
	}

	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
