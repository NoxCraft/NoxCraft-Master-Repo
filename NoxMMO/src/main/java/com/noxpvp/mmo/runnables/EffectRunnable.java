package com.noxpvp.mmo.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class EffectRunnable extends BukkitRunnable{
	
	private Entity e;
	private Effect effectType;
	private int bitData;
	private int runs;
	
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
