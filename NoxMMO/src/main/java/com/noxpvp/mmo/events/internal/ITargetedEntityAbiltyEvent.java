package com.noxpvp.mmo.events.internal;

import org.bukkit.entity.LivingEntity;

import com.noxpvp.mmo.abilities.BaseTargetedEntityAbility;
import com.noxpvp.mmo.abilities.ITargetedEntityAbility;

public interface ITargetedEntityAbiltyEvent extends IRangedEntityAbilityEvent {

	/**
	 * Gets the {@link ITargetedEntityAbility} associated with this event
	 * @return {@link BaseTargetedEntityAbility} The Ability
	 */
	public BaseTargetedEntityAbility getAbility();
	
	/**
	 * Gets the target {@link LivingEntity} involved in this event
	 * @return {@link LivingEntity} The target
	 */
	public LivingEntity getTarget();
}
