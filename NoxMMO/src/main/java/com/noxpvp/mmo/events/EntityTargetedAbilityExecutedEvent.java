package com.noxpvp.mmo.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.noxpvp.mmo.abilities.BaseTargetedEntityAbility;
import com.noxpvp.mmo.events.internal.ITargetedEntityAbiltyEvent;

public class EntityTargetedAbilityExecutedEvent extends EntityAbilityEvent implements ITargetedEntityAbiltyEvent {

	public EntityTargetedAbilityExecutedEvent(Entity what, BaseTargetedEntityAbility ability) {
		super(what, ability);
	}

	@Override
	public BaseTargetedEntityAbility getAbility() {
		return (BaseTargetedEntityAbility) super.getAbility();
	}

	public LivingEntity getTarget() {
		return ((BaseTargetedEntityAbility) ability).getTarget();
	}
}
