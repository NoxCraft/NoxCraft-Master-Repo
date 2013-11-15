package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

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
	
	public double getDistance(){
		return (getTarget() != null) ? target_ref.get().getLocation().distance(getPlayer().getLocation()) : -1;
	}
}
