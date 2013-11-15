package com.noxpvp.mmo.abilities;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface TargetedAbility extends Ability{
	
	/**
	 * Gets the target involved in this targeted ability instance
	 * 
	 * @return LivingEntity The target entity
	 */
	public LivingEntity getTarget();
	
	/**
	 * Gets the distance from target to the location provided
	 * 
	 * @return double The distance. Will return -1 if the target is null
	 */
	public double getDistance(Location loc);

}
