package com.noxpvp.mmo.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.noxpvp.mmo.abilities.BaseAbility;
import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;
import com.noxpvp.mmo.events.internal.IAbilityEvent;

public class AbilityEvent extends Event implements IAbilityEvent {

	private static final HandlerList handlers = new HandlerList();
	private final AbilityResult result;

	public AbilityEvent(AbilityResult result) {
		this.result = result;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public BaseAbility getAbility() {
		return (BaseAbility) result.getExecuter();
	}
	
	public AbilityResult getResult() {
		return result;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
