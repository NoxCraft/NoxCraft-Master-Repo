package com.noxpvp.mmo.events.internal;

import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.IEntityAbility;

public interface IEntityAbilityEvent extends IAbilityEvent {
	
	/**
	 * Gets the {@link IEntityAbility} associated with this event
	 * @return {@link BaseEntityAbility} The ability
	 */
	public BaseEntityAbility getAbility();

}
