package com.noxpvp.mmo.abilities;

public abstract class BaseAbility implements Ability {
	private final String name;
	
	public BaseAbility(final String name)
	{
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
