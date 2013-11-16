package com.noxpvp.mmo.runnables;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 *
 */
public class EntityEffectRunnable extends BukkitRunnable{
	
	private Entity e;
	private EntityEffect effectType;
	private int runs;
	
	/**
	 * 
	 * @param entity Entity to apply to effect to
	 * @param effect Effect type
	 * @param runsLimit The amount of times to run, if used as a task timer
	 */
	public EntityEffectRunnable(Entity entity, EntityEffect effect, int runsLimit){
		this.e = entity;
		this.effectType = effect;
		this.runs = runsLimit;
	}

	public void safeCancel() { try { cancel(); } catch (IllegalStateException e) {} }
	
	public void run(){
		if (e == null)
		{
			safeCancel();
			return;
		}
		int runs = 0;
		
		if (runs < this.runs)
			e.playEffect(effectType);
		
		else Bukkit.getScheduler().cancelTask(this.getTaskId());
	}
	
}