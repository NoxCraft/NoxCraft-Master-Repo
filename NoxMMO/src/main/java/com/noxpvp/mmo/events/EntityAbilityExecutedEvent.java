package com.noxpvp.mmo.events;

import org.bukkit.entity.Entity;

import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class EntityAbilityExecutedEvent extends EntityAbilityEvent {
	private AbilityResult result;
	
	public EntityAbilityExecutedEvent(Entity what, AbilityResult result) {
		super(what, (BaseEntityAbility) result.getExecuter());

		this.result = result;
	}

	/**
	 * Gets the result of the ability that caused this event
	 * 
	 * @return {@link AbilityResult} the result
	 */
	public AbilityResult getResult() {
		return result;
	}
}
