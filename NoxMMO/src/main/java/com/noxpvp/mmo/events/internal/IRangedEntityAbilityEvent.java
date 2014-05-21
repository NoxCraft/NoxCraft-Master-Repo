package com.noxpvp.mmo.events.internal;

import com.noxpvp.mmo.abilities.BaseRangedEntityAbility;

public interface IRangedEntityAbilityEvent extends IEntityAbilityEvent {
	
	/**
	 * Gets the Ranged ability associated with this event
	 * @return {@link BaseRangedEntityAbility} The ability
	 */
	public BaseRangedEntityAbility getAbility();
}
