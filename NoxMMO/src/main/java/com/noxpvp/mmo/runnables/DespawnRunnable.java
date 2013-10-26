package com.noxpvp.mmo.runnables;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class DespawnRunnable extends BukkitRunnable{
	
	private Entity e;
	
	public DespawnRunnable(Entity entity){
		this.e = entity;
	}
	
	public void run(){
		e.remove();
	}

}
