package com.noxpvp.mmo.runnables;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.CommonUtil;

/**
 * @author NoxPVP
 */
public class HealRunnable extends BukkitRunnable {

	private double health;
	private LivingEntity e;
	private int runsLimit;
	private int runs = 0;

	/**
	 * @param entity       Living entity to heal
	 * @param healthAmount double amount to heal target
	 * @param runsLimit    the amount of times to run, if used as a tasktimer
	 */
	public HealRunnable(LivingEntity entity, double healthAmount, int runsLimit) {
		this.e = entity;
		this.health = healthAmount;
		this.runsLimit = runsLimit;
	}

	public void safeCancel() {
		try {
			cancel();
		} catch (IllegalStateException e) {
		}
	}

	public void run() {
		if (runs++ >= runsLimit) {
			safeCancel();
			return;
		}

		double ha = e.getHealth() + health;
		ha = ha > e.getMaxHealth() ? e.getMaxHealth() : ha;

		if (CommonUtil.callEvent(new EntityRegainHealthEvent(e, ha - e.getHealth(), RegainReason.CUSTOM)).isCancelled())
			return;

		e.setHealth(ha);
	}

}
