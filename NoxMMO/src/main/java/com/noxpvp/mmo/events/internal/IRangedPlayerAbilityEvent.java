package com.noxpvp.mmo.events.internal;

import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.abilities.IRangedPlayerAbility;

public interface IRangedPlayerAbilityEvent extends IPlayerAbilityEvent {

	/**
	 * Gets the {@link IRangedPlayerAbility} associated with this event
	 *
	 * @return {@link BaseRangedPlayerAbility} The ability
	 */
	public BaseRangedPlayerAbility getAbility();
}
