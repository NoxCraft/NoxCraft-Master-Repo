package com.noxpvp.mmo.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.noxpvp.mmo.NoxMMO;

public class ExpandingDamageRunnable extends BukkitRunnable{
	private final LivingEntity a;
	private final Location loc;
	private final double d;
	private final int range;
	private long speed;
	
	private List<Damageable> damaged;
	private int runs;
	
	public ExpandingDamageRunnable(LivingEntity attacker, Location loc, double damage, int range, int speed){
		this.a = attacker;
		this.loc = loc;
		this.d = damage;
		this.range = range;
		
		this.damaged = new ArrayList<Damageable>();
		this.runs = 0;
	}
	
	public void safeCancel() { try { cancel(); } catch (IllegalStateException e) {} }
	
	public void start(int delay) {
		runTaskTimer(NoxMMO.getInstance(), delay, speed);
	}
	
	public void run(){
		if (runs++ >= range){
			safeCancel();
			return;
		}
		
		for (Entity e : WorldUtil.getNearbyEntities(loc, runs, runs, runs)){
			if (e instanceof Damageable && !damaged.contains((Damageable) e)){
				if (e == a) continue;
				
				Damageable it = (Damageable) e;
				
				it.damage(d, a);
				damaged.add(it);
			}
			
		}
	}
}
