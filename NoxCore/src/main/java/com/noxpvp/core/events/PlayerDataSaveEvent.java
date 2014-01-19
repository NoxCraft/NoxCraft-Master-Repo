package com.noxpvp.core.events;

import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

public class PlayerDataSaveEvent extends NoxPlayerDataEvent{
	public PlayerDataSaveEvent(NoxPlayerAdapter player, boolean honorCore)
	{
		super(player, honorCore);
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
