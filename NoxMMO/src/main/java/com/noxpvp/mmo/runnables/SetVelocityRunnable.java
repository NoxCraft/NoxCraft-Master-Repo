package com.noxpvp.mmo.runnables;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * @author NoxPVP
 *
 */
public class SetVelocityRunnable extends BukkitRunnable{
	
	private Entity e;
	private Vector velo;
	
	/**
	 * 
	 * @param entity Entity to give the new velocity
	 * @param newVelo Velocity to set
	 */
	public SetVelocityRunnable(Entity entity, Vector newVelo){
		this.e = entity;
		this.velo = newVelo;
	}

	public void run(){
		e.setVelocity(velo);
	}
}
