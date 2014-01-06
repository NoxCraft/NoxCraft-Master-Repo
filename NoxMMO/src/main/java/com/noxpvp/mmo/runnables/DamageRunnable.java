package com.noxpvp.mmo.runnables;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 *
 */
public class DamageRunnable extends BukkitRunnable{
	
	public static Map<String, DamageRunnable> withPlayers = new HashMap<String, DamageRunnable>();
	
	private Damageable e;
	private Entity a;
	private double d;
	private int runs;
	private int runLimit;
	
	/**
	 * 
	 * @param entity The damagable entity to damage
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
		if (runs == 0 && e instanceof Player) {
			Player p = (Player) e;
			
			if (withPlayers.containsKey(p.getName())) {
				withPlayers.get(p).safeCancel();
			}
			
			withPlayers.put(p.getName(), this);
		}
		
		if (runs++ >= runLimit){
			safeCancel();
			return;
		}
		
		if (!(e instanceof Damageable || e.isDead())){
			safeCancel();
			return;
		}
		if (a != null)
			e.damage(d, a);
		else
			e.damage(d);
	}

}
