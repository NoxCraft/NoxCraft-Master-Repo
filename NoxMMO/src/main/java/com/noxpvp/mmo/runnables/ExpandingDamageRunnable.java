package com.noxpvp.mmo.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.mmo.NoxMMO;

public class ExpandingDamageRunnable extends BukkitRunnable{
	private final LivingEntity a;
	private final double d;
	private final int range;
	private long speed;
	
	private int runs;
	private int i;
	
	public ExpandingDamageRunnable(LivingEntity attacker, double damage, int range, int speed){
		this.a = attacker;
		this.d = damage;
		this.range = range;
		
		this.runs = 0;
		this.i = 0;
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
		
		if (i++ <= runs) {
			// do next ring
			int bx = (int) a.getLocation().getX();
			int y = (int) a.getLocation().getY();
			int bz = (int) a.getLocation().getZ();
			
			List<Location> blcks = new ArrayList<>();
			
			for (int x = bx - i; x <= bx + i; x++) {
				
				
				for (int z = bz - i; z <= bz + i; z++) {
					if ((Math.abs(x-bx) == i || Math.abs(z-bz) == i)){
						blcks.add(new Location(a.getWorld(), x, y, z));
					}
				}
					
				for  (Entity e : a.getNearbyEntities(range, range, range)) {
					if(!(e instanceof Damageable)) continue;
					
					for (Location loc : blcks){
						if ( (Math.abs(loc.getX()) == e.getLocation().getX()) && 
								(Math.abs(loc.getY()) == e.getLocation().getY()) &&
									(Math.abs(loc.getZ()) == e.getLocation().getZ()) ) {
							
							((Damageable)e).damage(d, a);
						}
					}
				}
			}
		}
	}
}
