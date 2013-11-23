package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public abstract class BaseTargetedAbility extends BaseAbility implements TargetedAbility{
	private Reference<LivingEntity> target_ref;
	
	public BaseTargetedAbility(String name, LivingEntity target){
		super(name);
		
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
	
	public BaseTargetedAbility(String name){
		this(name, null);
	}
	
	public LivingEntity getTarget() {
		return (target_ref == null) ? null : target_ref.get();
	}
	
	public void setTarget(LivingEntity target){
		this.target_ref = new SoftReference<LivingEntity>(target);
	}
}
