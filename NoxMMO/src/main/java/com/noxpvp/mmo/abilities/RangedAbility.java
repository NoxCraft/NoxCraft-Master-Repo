package com.noxpvp.mmo.abilities;

public interface RangedAbility extends Ability {
	
	/**
	 * Gets the range for this ability
	 * 
	 * @return {@link Double} the current range limit
	 */
	public double getRange();
	
	/**
	 * Sets the max range for this ability, the max distance the player and its ranged object
	 * 
	 * @param range
	 */
	public void setRange(double range);

}
