package com.noxpvp.mmo.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;
import com.noxpvp.mmo.abilities.BaseTargetedEntityAbility;
import com.noxpvp.mmo.events.internal.ITargetedEntityAbiltyEvent;

public class EntityTargetedAbilityExecutedEvent extends EntityAbilityExecutedEvent implements ITargetedEntityAbiltyEvent {
	
	public EntityTargetedAbilityExecutedEvent(Entity what, AbilityResult result) {
		super(what, result);

	}

	@Override
	public BaseTargetedEntityAbility getAbility() {
		return (BaseTargetedEntityAbility) super.getAbility();
	}

	public LivingEntity getTarget() {
		return getAbility().getTarget();
	}

}
