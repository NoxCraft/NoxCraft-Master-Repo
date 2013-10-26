package com.noxpvp.mmo.runnables;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SetVelocityRunnable extends BukkitRunnable{
	
	private Entity e;
	private Vector velo;
	
	public SetVelocityRunnable(Entity entity, Vector newVelo){
		this.e = entity;
		this.velo = newVelo;
	}

	public void run(){
		e.setVelocity(velo);
	}
}
