package com.noxpvp.mmo.runnables;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class HealRunnable extends BukkitRunnable{
	
	private double health;
	private LivingEntity e;
	private int runsLimit;
	private int runs = 0;
	
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
		
		if (!(e instanceof LivingEntity))
		{
			safeCancel();
			return;
		}
		
		health = (e.getHealth() + health);
		try {
			e.setHealth(e.getHealth() + health);
		}
		catch (IllegalArgumentException health) {}
		}
	}

}
