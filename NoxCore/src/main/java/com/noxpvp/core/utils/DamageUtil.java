package com.noxpvp.core.utils;


import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageUtil {
	
	public static Player getDamagedPlayer(EntityDamageEvent event) {
		LivingEntity e = getDamagedEntity(event);
		
		if (e instanceof Player)
			return (Player) e;
		return null;
	}
	
	public static LivingEntity getDamagedEntity(EntityDamageEvent event) {
		Entity e = event.getEntity();
		return ((e instanceof LivingEntity)? (LivingEntity)e : null);
	}
	
	public static Player getAttackingPlayer(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent)
			return getAttackingPlayer((EntityDamageByEntityEvent)event);
		return null;
	}
	
	public static Player getAttackingPlayer(EntityDamageByEntityEvent event) {
		LivingEntity entity = getAttackingEntity(event);
		
		return (entity instanceof Player)? (Player) entity : null;
	}
	
	public static LivingEntity getAttackingEntity(EntityDamageEvent event){
		if (event instanceof EntityDamageByEntityEvent)
			return getAttackingEntity((EntityDamageByEntityEvent) event);
		return null;
	}
	
	public static LivingEntity getAttackingEntity(EntityDamageByEntityEvent event) {
		Entity e = event.getDamager();
		
		return (e instanceof LivingEntity)?(LivingEntity) e:null;
	}

}