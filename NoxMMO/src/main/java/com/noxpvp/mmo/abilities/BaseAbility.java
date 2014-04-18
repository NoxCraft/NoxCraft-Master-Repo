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
	
	public String getDescription() {
		return "";
	}
}
