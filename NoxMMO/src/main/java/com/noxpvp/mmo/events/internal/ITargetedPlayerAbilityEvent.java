package com.noxpvp.mmo.events.internal;

import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.ITargetedPlayerAbility;

public interface ITargetedPlayerAbilityEvent extends IRangedPlayerAbilityEvent {

	/**
	 * Gets the {@link ITargetedPlayerAbility} associated with this event
	 * @return {@link BaseTargetedPlayerAbility} The ability
	 */
	public BaseTargetedPlayerAbility getAbility();
}
