package com.noxpvp.mmo.events.internal;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPlayerAbility;

public interface IPlayerAbilityEvent extends IAbilityEvent {

	/**
	 * Gets the {@link IPlayerAbility} associated with this event
	 *
	 * @return {@link BasePlayerAbility} The ability
	 */
	public BasePlayerAbility getAbility();

}
