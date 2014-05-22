package com.noxpvp.mmo.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

import com.noxpvp.mmo.abilities.BaseAbility;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.events.internal.IAbilityEvent;

public class EntityAbilityEvent extends EntityEvent implements IAbilityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected BaseEntityAbility ability;
	
	public EntityAbilityEvent(Entity what, BaseEntityAbility ability) {
		super(what);
		
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
