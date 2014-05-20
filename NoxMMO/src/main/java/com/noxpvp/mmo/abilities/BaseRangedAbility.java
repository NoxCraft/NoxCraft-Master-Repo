package com.noxpvp.mmo.abilities;

public abstract class BaseRangedAbility extends BaseAbility implements RangedAbility {

	private double range;
	
	public BaseRangedAbility(String name) {
		this(name, 0);
	}
	
	public BaseRangedAbility(String name, double range) {
		super(name);
		
		this.range = range;
	}
	
	public double getRange() {
		return range;
	}
	
	public void setRange(double range) {
		this.range = range;
	}
	
}
