package com.noxpvp.mmo.runnables;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityEffectRunnable extends BukkitRunnable{
	
	private Entity e;
	private EntityEffect effectType;
	private int runs;
	
	public EntityEffectRunnable(Entity entity, EntityEffect effect, int runsLimit){
		this.e = entity;
		this.effectType = effect;
		this.runs = runsLimit;
	}

	public void run(){
		if (e == null)
			return;
		
		int runs = 0;
		
		if (runs < this.runs)
			e.playEffect(effectType);
		
		else Bukkit.getScheduler().cancelTask(this.getTaskId());
	}
	
}