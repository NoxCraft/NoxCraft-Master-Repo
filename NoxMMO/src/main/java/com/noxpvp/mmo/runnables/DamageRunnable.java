package com.noxpvp.mmo.runnables;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class DamageRunnable extends BukkitRunnable{
	
	private Damageable e;
	private Entity a;
	private double d;
	
	public DamageRunnable(Damageable entity, Entity Attacker, double damage){
		this.e = entity;
		this.a = Attacker;
		this.d = damage;
	}
	
	public void safeCancel() { try { cancel(); } catch (IllegalStateException e) {} }
	
	public void run(){
		if (!(e instanceof Damageable)){
			safeCancel();
			return;
		}
		e.damage(d, a);
	}

}
