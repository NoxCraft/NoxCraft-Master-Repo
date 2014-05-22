package com.noxpvp.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CooldownExpireEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private final String name;

	public CooldownExpireEvent(Player player, String cooldownName) {
		super(player);
		this.name = cooldownName;
	}

	/**
	 * Gets the handler list.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Gets the handlers.
	 *
	 * @return the handlers
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public String getName() {
		return name;
	}
}
