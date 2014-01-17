package com.noxpvp.core.events;

import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

public class PlayerDataSaveEvent extends NoxPlayerDataEvent{
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerDataSaveEvent(NoxPlayerAdapter player, boolean honorCore)
	{
		super(player, honorCore);
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
