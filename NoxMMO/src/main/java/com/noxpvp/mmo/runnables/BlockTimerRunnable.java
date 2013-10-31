package com.noxpvp.mmo.runnables;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author NoxPVP
 *
 */
public class BlockTimerRunnable extends BukkitRunnable{
	
	private Material newType;
	private Material oldType;
	private Block block;
	
	/**
	 * 
	 * @param block - The block to change
	 * @param newType - The material the block should be changed to
	 * @param oldType - The material the block should be changed from. If the block given is not this type
	 * is will not be changed as something else is the world has changed it already. (set this to null for no checking)
	 */
	public BlockTimerRunnable(Block block, Material newType, Material oldType){
		this.newType = newType;
		this.oldType = oldType;
		this.block = block;
	}
	
	public void safeCancel() { try { cancel(); } catch (IllegalStateException e) { } }
	
	public void run(){
		if (oldType != null && block.getType() != oldType)
		{
			safeCancel();
			return;
		}
		block.setType(newType);
	}
}
