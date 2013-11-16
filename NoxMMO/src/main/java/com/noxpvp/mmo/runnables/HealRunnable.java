package com.noxpvp.mmo.runnables;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 *
 */
public class HealRunnable extends BukkitRunnable{
	
	private double health;
	private LivingEntity e;
	private int runsLimit;
	private int runs = 0;
	
	/**
	 * 
	 * @param entity Living entity to heal
	 * @param healthAmount double amount to heal target
	 * @param runsLimit the amount of times to run, if used as a tasktimer
	 */
	public HealRunnable(LivingEntity entity, double healthAmount, int runsLimit){
		this.e = entity;
		this.health = healthAmount;
		this.runsLimit = runsLimit;
	}
	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	
	public void run(){
		if (!(runs >= runsLimit))
		{
			safeCancel();
			return;
		}
		
		e.setHealth(e.getHealth() + health);
	}

}
