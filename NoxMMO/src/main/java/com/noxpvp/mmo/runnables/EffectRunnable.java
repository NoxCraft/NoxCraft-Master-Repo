package com.noxpvp.mmo.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 *
 */
public class EffectRunnable extends BukkitRunnable{
	
	private Entity e;
	private Effect effectType;
	private int bitData;
	private int runs;
	
	/**
	 * 
	 * @param entity The entity at which location and world to apply the effect
	 * @param effect The effect type
	 * @param bitdata the extra bit data for specific effect exp: potion splash color
	 * @param runsLimit The amount of times to run, if used in a task timer
	 */
	public EffectRunnable(Entity entity, Effect effect, int bitdata, int runsLimit){
		this.e = entity;
		this.effectType = effect;
		this.bitData = bitdata;
		this.runs = runsLimit;
	}

	public void safeCancel() {try { cancel(); } catch (IllegalStateException e) {}	}
	
	public void run(){
		if (e == null)
		{
			safeCancel();
			return;
		}
		
		int runs = 0;
		
		if (runs < this.runs)
			e.getWorld().playEffect(e.getLocation(), effectType, bitData);
		
		else Bukkit.getScheduler().cancelTask(this.getTaskId());
	}
	
}
