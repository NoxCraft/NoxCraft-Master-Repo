package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Entity;

public abstract class BaseRangedEntityAbility extends BaseEntityAbility implements IRangedEntityAbility {
	private double range;

	public BaseRangedEntityAbility(String name, Entity ref) {
		this(name, ref, 0);
	}

	public BaseRangedEntityAbility(String name, Entity ref, double range) {
		super(name, ref);

		this.range = range;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

}
