package com.noxpvp.core.events;

import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

@Deprecated
public class PlayerDataSaveEvent extends NoxPlayerDataEvent{
	@Deprecated
	public PlayerDataSaveEvent(NoxPlayerAdapter player, boolean honorCore)
	{
		super(player, honorCore);
	}
	
	@Deprecated
	public HandlerList getHandlers() {
		return handlers;
	}

	@Deprecated
	private static final HandlerList handlers = new HandlerList();

	@Deprecated
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
