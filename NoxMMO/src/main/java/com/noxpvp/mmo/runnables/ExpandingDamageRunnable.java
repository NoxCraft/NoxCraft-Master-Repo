package com.noxpvp.mmo.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class ExpandingDamageRunnable extends BukkitRunnable{
	private List<Damageable> damaged;
	private final LivingEntity a;
	private final double d;
	private final int range;
	private int runs = 0;
	
	public ExpandingDamageRunnable(LivingEntity attacker, double damage, int range){
		this.a = attacker;
		this.d = damage;
		this.range = range;
		this.damaged = new ArrayList<Damageable>();
	}
	
	public void safeCancel() { try { cancel(); } catch (IllegalStateException e) {} }
	
	public void run(){
		if (runs++ >= range){
			safeCancel();
			return;
		}
		
		for (Entity it : a.getNearbyEntities(range, range, range)){
			if (!(it instanceof Damageable)) continue;
			if (damaged.contains(it)) continue;
			
			if (NumberConversions.toInt(a.getLocation().distance(it.getLocation())) <= runs){
				damaged.add((Damageable) it);
				((Damageable) it).damage(d, a);
			}
		}
	}

}
