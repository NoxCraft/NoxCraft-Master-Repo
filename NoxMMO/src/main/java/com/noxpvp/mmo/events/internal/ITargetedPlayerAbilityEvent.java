package com.noxpvp.mmo.events.internal;

import org.bukkit.entity.LivingEntity;

import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.ITargetedPlayerAbility;

public interface ITargetedPlayerAbilityEvent extends IRangedPlayerAbilityEvent {

	/**
	 * Gets the {@link ITargetedPlayerAbility} associated with this event
	 * @return {@link BaseTargetedPlayerAbility} The ability
	 */
	public BaseTargetedPlayerAbility getAbility();
	
	/**
	 * Gets the target {@link LivingEntity} involved in this event
	 * @return {@link LivingEntity} The target
	 */
	public LivingEntity getTarget();
}
