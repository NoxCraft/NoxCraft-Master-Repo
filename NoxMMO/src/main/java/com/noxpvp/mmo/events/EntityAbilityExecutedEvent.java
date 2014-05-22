package com.noxpvp.mmo.events;

import org.bukkit.entity.Entity;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class EntityAbilityExecutedEvent extends EntityAbilityEvent {

	public EntityAbilityExecutedEvent(Entity what, BaseEntityAbility ability) {
		super(what, ability);

	}

}
