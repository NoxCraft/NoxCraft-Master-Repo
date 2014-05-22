package com.noxpvp.mmo.events.internal;

import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.BaseAbility;

public interface IAbilityEvent {

	/**
	 * Get the {@link Ability} associated with this event
	 *
	 * @return {@link BaseAbility} The ability
	 */
	public BaseAbility getAbility();

}
