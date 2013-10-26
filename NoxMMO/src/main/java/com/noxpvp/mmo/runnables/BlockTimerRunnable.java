package com.noxpvp.mmo.runnables;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockTimerRunnable extends BukkitRunnable{
	
	private Material newType;
	private Material oldType;
	private Block block;
	public BlockTimerRunnable(Block block, Material newType, Material oldType){
		this.newType = newType;
		this.oldType = oldType;
		this.block = block;
	}
	
	public void run(){
		if (block.getType() != oldType)
			return;
		
		block.setType(newType);
	}
}
