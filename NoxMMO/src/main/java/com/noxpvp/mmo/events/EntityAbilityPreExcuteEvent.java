package com.noxpvp.mmo.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class EntityAbilityPreExcuteEvent extends EntityAbilityEvent implements Cancellable {
	private boolean cancelled;
	
	public EntityAbilityPreExcuteEvent(Entity what, BaseEntityAbility ability) {
		super(what, ability);
		
		this.cancelled = false;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
}
