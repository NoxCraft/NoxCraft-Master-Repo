package com.noxpvp.mmo.events.internal;

import com.noxpvp.mmo.abilities.BaseTargetedEntityAbility;
import com.noxpvp.mmo.abilities.ITargetedEntityAbility;

public interface ITargetedEntityAbiltyEvent extends IRangedEntityAbilityEvent {

	/**
	 * Gets the {@link ITargetedEntityAbility} associated with this event
	 * @return {@link BaseTargetedEntityAbility} The Ability
	 */
	public BaseTargetedEntityAbility getAbility();
}
