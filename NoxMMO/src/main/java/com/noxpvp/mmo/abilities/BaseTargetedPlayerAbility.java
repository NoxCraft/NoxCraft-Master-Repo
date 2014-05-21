package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class BaseTargetedPlayerAbility extends BaseRangedPlayerAbility implements ITargetedPlayerAbility {
	private Reference<LivingEntity> target_ref;
	
	public BaseTargetedPlayerAbility(String name, Player player, double range, LivingEntity target){
		super(name, player, range);
		
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	/**
	 * creates a new targeted player ability with 0 range
	 * 
	 * @param name
	 * @param player
	 * @param target
	 */
	public BaseTargetedPlayerAbility(String name, Player player, LivingEntity target){
		this(name, player, 0, target);
	}
	
	public LivingEntity getTarget() {
		return (target_ref == null) ? null : target_ref.get();
	}
	
	public void setTarget(LivingEntity target){
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public double getDistance(){
		if (getPlayer() != null)
			return getDistance(getPlayer().getLocation());
		
		return -1;
	}
	
	public double getDistance(Location loc) {
		if (getTarget() != null && loc != null)
			return getTarget().getLocation().distance(loc);
		
		return -1;
	}
	
	/**
	 * Returns is the player or target of this ability is null, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return super.mayExecute() && (getTarget() != null && (getDistance() <= getRange()));
	}
}
