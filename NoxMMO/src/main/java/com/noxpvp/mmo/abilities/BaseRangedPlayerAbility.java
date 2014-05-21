package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Player;

public abstract class BaseRangedPlayerAbility extends BasePlayerAbility implements IRangedPlayerAbility {
	private double range;
	
	public BaseRangedPlayerAbility(String name, Player player) {
		super(name, player);
	}
	
	public BaseRangedPlayerAbility(String name, Player player, double range) {
		super(name, player);
		
		this.range = range;
	}

	public void setRange(double range) {
		this.range = range;
	}
	
	public double getRange() {
		return range;
	}
}
