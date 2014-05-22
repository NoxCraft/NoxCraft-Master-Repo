package com.noxpvp.mmo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class PlayerAbilityPreExecuteEvent extends PlayerAbilityEvent implements Cancellable {
	private boolean cancelled;

	public PlayerAbilityPreExecuteEvent(Player who, BasePlayerAbility ability) {
		super(who, ability);

		this.cancelled = false;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
