/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.events.internal;

import org.bukkit.entity.LivingEntity;

import com.noxpvp.mmo.abilities.BaseTargetedEntityAbility;
import com.noxpvp.mmo.abilities.ITargetedEntityAbility;

public interface ITargetedEntityAbiltyEvent extends IRangedEntityAbilityEvent {

	/**
	 * Gets the {@link ITargetedEntityAbility} associated with this event
	 *
	 * @return {@link BaseTargetedEntityAbility} The Ability
	 */
	public BaseTargetedEntityAbility getAbility();

	/**
	 * Gets the target {@link LivingEntity} involved in this event
	 *
	 * @return {@link LivingEntity} The target
	 */
	public LivingEntity getTarget();
}
