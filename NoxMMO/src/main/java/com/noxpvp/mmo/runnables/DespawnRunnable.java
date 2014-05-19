package com.noxpvp.mmo.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 *
 */
public class DespawnRunnable extends BukkitRunnable{
	
	private List<Entity> entitys;
	
	/**
	 * 
	 * @param entity The entity to mark to be removed
	 */
	public DespawnRunnable(Entity entity){
		this.entitys = new ArrayList<Entity>();
		this.entitys.add(entity);
	}
	
	/**
	 * 
	 * @param entitys A list of entities to mark to be removed.
	 */
	public DespawnRunnable(List<Entity> entitys){
		this.entitys = entitys;
	}
	
	public void run(){
		for (Entity e : this.entitys){
			if (e == null) continue;
				
			e.remove();
		}
	}

}
