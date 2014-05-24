package com.noxpvp.mmo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.events.internal.IPlayerAbilityEvent;

public abstract class PlayerAbilityEvent extends PlayerEvent implements IPlayerAbilityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected BasePlayerAbility ability;

	public PlayerAbilityEvent(Player who, BasePlayerAbility ability) {
		super(who);

		this.ability = ability;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public BasePlayerAbility getAbility() {
		return ability;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
