package com.noxpvp.mmo.runnables;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VanishPlayerRunnable extends BukkitRunnable{
	private Player vanisher;
	private int runs;
	private double range;
	private int n;
	
	/**
	 * 
	 * @param vanisherThe player to keep vanished
	 * @param rangeThe distance to look for other players
	 * @param runsThe amount of time to run
	 */
	public VanishPlayerRunnable(Player vanisher, double range, int runs){
		this.runs = runs;
		this.range = range;
		this.n = 0;
	}
	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	
	public void run(){
		if (this.runs >= this.n){
			for (Player it : vanisher.getServer().getOnlinePlayers()){
				if (it.canSee(vanisher)) continue;
				it.showPlayer(vanisher);
			}
			safeCancel();
			return;
		}
		
		for (Entity it : this.vanisher.getNearbyEntities(range, range, range)){
			if (!(it instanceof Player)) continue;
			if (!((Player) it).canSee(this.vanisher)) continue;
			
			((Player) it).hidePlayer(vanisher);
		}
	}

}
