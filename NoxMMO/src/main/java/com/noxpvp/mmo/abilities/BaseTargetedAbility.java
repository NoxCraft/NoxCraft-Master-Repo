package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public abstract class BaseTargetedAbility extends BaseRangedAbility implements ITargetedAbility {
	
	private Reference<LivingEntity> target_ref;
	
	public BaseTargetedAbility(String name, double range, LivingEntity target){
		super(name, range);
		
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	/**
	 * creates a new targeted ability with 0 range
	 * 
	 * @param name
	 * @param target
	 */
	public BaseTargetedAbility(String name, LivingEntity target){
		this(name, 0, target);
	}
	
	/**
	 * creates a new targeted ability with 0 range and a null target
	 * 
	 * @param name
	 */
	public BaseTargetedAbility(String name) {
		this(name, null);
	}
	
	public LivingEntity getTarget() {
		return (target_ref == null) ? null : target_ref.get();
	}
	
	public void setTarget(LivingEntity target){
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public double getDistance(Location loc) {
		if (getTarget() != null && loc != null)
			return getTarget().getLocation().distance(loc);
		
		return -1;
	}
	
	/**
	 * Returns is the target of this ability is null, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return getTarget() != null;
	}
}
