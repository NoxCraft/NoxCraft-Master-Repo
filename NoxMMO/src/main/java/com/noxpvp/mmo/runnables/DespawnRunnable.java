package com.noxpvp.mmo.runnables;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 *
 */
public class DespawnRunnable extends BukkitRunnable{
	
	private Entity e;
	
	/**
	 * 
	 * @param entity - The entity to mark to be removed
	 */
	public DespawnRunnable(Entity entity){
		this.e = entity;
	}
	
	public void run(){
		if (e != null)
			e.remove();
		else return;
	}

}
