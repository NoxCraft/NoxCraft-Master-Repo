package com.noxpvp.mmo.runnables;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.PlayerUtil;

public class VanishPlayerRunnable extends BukkitRunnable{
	private Player vanisher;
	private int runs;
	private double range;
	private int runsLimit;
	
	/**
	 * 
	 * @param vanisher The player to keep vanished
	 * @param range The distance to look for other players
	 * @param runs The amount of time to run
	 */
	public VanishPlayerRunnable(Player vanisher, double range, int runs){
		this.runs = 0;
		this.range = range;
		this.runsLimit = runs;
	}
	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	
	public void run(){
		if (this.runs++ >= runsLimit) {
			for (Player it : vanisher.getServer().getOnlinePlayers())
				it.showPlayer(vanisher);
				
			safeCancel();
			return;
		}
		
		for (Entity it : PlayerUtil.getNearbyPlayers(vanisher, range))
			((Player)it).hidePlayer(vanisher);
	}

}
