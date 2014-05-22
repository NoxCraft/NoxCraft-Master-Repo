package com.noxpvp.mmo.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.noxpvp.mmo.abilities.BaseAbility;
import com.noxpvp.mmo.events.internal.IAbilityEvent;

public class AbilityEvent extends Event implements IAbilityEvent {

	private final static HandlerList handlers = new HandlerList();
	private final BaseAbility ability;
	
	public AbilityEvent(BaseAbility ability) {
		this.ability = ability;
	}

	public BaseAbility getAbility() {
		return ability;
	}

	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
