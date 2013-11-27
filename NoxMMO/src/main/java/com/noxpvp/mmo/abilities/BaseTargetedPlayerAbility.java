package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class BaseTargetedPlayerAbility extends BasePlayerAbility implements TargetedPlayerAbility{
	private Reference<LivingEntity> target_ref;
	
	public BaseTargetedPlayerAbility(String name, Player player, LivingEntity target){
		super(name, player);
		
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public BaseTargetedPlayerAbility(String name, Player player){
		this(name, player, null);
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
}
