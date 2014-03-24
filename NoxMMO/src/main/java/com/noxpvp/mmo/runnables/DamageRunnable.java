package com.noxpvp.mmo.runnables;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 *
 */
public class DamageRunnable extends BukkitRunnable{
	
	public static WeakHashMap<WeakReference<Damageable>, DamageRunnable> running = new WeakHashMap<WeakReference<Damageable>, DamageRunnable>();
	
	private Damageable e;
	private Entity a;
	private double d;
	private int runs;
	private int runLimit;
	
	/**
	 * 
	 * @param entity The damageable entity to damage
	 * @param Attacker The attacker (if any)
	 * @param damage the damage to apply
	 * @param runs The amount of times to run this, if used in a task timer (set to atleast 1)
	 */
	public DamageRunnable(Damageable entity, Entity Attacker, double damage, int runs){
		this.e = entity;
		this.a = Attacker;
		this.d = damage;
		this.runs = 0;
		this.runLimit = runs;
	}
	
	public void safeCancel() { try { cancel(); } catch (IllegalStateException e) {} }
	
	public void run(){
		if (runs == 0){
			if (running.containsKey(e)) {
				running.get(e).safeCancel();
			}
			
			running.put(new WeakReference<Damageable>(e), this);
		}
		
		if (runs++ >= runLimit){
			safeCancel();
			return;
		}
		
		if (!(e instanceof Damageable) || e.isDead()){
			safeCancel();
			return;
		}
		
		if (a != null)
			e.damage(d, a);
		else
			e.damage(d);
	}

}
