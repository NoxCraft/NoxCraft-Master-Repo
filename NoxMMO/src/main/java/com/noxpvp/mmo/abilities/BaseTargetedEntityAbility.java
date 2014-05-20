package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public abstract class BaseTargetedEntityAbility extends BaseRangedEntityAbility implements TargetedEntityAbility {
	private Reference<LivingEntity> target_ref;
	
	public BaseTargetedEntityAbility(String name, Entity entity, double range, LivingEntity target) {
		super(name, entity, range);
		
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	/**
	 * creates a new targeted entity ability with 0 range
	 * 
	 * @param name
	 * @param entity
	 * @param target
	 */
	public BaseTargetedEntityAbility(String name, Entity entity, LivingEntity target) {
		this(name, entity, 0, target);
	}
	
	public LivingEntity getTarget() {
		return (target_ref == null) ? null : target_ref.get();
	}
	
	public void setTarget(LivingEntity target) {
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public double getDistance() {
		if (getEntity() != null)
			return getDistance(getEntity().getLocation());
		
		return -1;
	}
	
	public double getDistance(Location loc) {
		if (getTarget() != null && loc != null)
			return getTarget().getLocation().distance(loc);
		
		return -1;
	}
	
	/**
	 * Returns is the entity or target of this ability is null, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return super.mayExecute() && (getEntity() != null && getTarget() != null && (getDistance() <= getRange()));
	}
}
