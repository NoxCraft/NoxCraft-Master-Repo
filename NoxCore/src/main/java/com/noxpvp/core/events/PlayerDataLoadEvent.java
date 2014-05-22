package com.noxpvp.core.events;

import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

@Deprecated
public class PlayerDataLoadEvent extends NoxPlayerDataEvent {
	private static final HandlerList handlers = new HandlerList();
	private boolean isOverwriting;

	@Deprecated
	public PlayerDataLoadEvent(NoxPlayerAdapter player, boolean honorCore) {
		this(player, honorCore, true);
	}

	@Deprecated
	public PlayerDataLoadEvent(NoxPlayerAdapter player, boolean honorCore, boolean shouldOverwrite) {
		super(player, honorCore);
		this.isOverwriting = shouldOverwrite;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Deprecated
	public boolean isOverwriting() {
		return isOverwriting;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
