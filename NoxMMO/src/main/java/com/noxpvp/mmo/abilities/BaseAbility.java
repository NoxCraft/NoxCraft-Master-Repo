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

package com.noxpvp.mmo.abilities;

import com.noxpvp.mmo.locale.MMOLocale;

public abstract class BaseAbility implements Ability {
	private final String name;
	
	public BaseAbility(final String name) {
		this.name = name;
	}
	
	/**
	 * This implementation returns {@link MMOLocale#ABIL_DISPLAY_NAME} using method {@link MMOLocale#get(String...)} with the parameters as [getName(), ""]
	 * <hr>
	 * {@inheritDoc}
	 */
	public String getDisplayName() {
		return MMOLocale.ABIL_DISPLAY_NAME.get(getName(), "");
	}
	
	public String getName() {
		return name;
	}
}
