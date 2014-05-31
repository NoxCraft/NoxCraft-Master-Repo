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
