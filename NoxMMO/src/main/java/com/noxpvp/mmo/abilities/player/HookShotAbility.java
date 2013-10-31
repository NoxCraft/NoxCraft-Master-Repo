package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class HookShotAbility extends BasePlayerAbility{
	
	public static List<Arrow> hookArrows = new ArrayList<Arrow>();
	
	private final static String ABILITY_NAME = "Hook Shot";
	private Arrow hook;
	private double maxDistance;
	private int blockTime;
	private Material holdingBlockType;
	
	/**
	 * 
	 * 
	 * @return Arrow hook - The current hook given to this instance. Returns null is setHook() has not been used
	 */
	public Arrow getHook() {return hook;}
	
	/**
	 * 
	 * 
	 * @param hook - The hook from a (intended) projectile shoot event
	 * @return HookShotAbility - This instance used for chaining
	 */
	public HookShotAbility setHook(Arrow hook) {this.hook = hook; return this;}

	/**
	 * 
	 * 
	 * @return int - Max distance set for ability execution - returns null is setMaxDistance() has not been used
	 */
	public double getMaxDistance() {return maxDistance;}
	
	/**
	 * 
	 * 
	 * @param maxDistance The max distance a player can hook to
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotAbility setMaxDistance(double maxDistance) {this.maxDistance = maxDistance; return this;}

	/**
	 * 
	 * 
	 * @return Material - Type of block used to support player
	 */
	public Material getHoldingBlockType() {return holdingBlockType;}
	
	/**
	 * 
	 * 
	 * @param block - Material type that should be used to support the player
	 * @return HookShotAbility - This instance used for chaining
	 */
	public HookShotAbility setHoldingBlockType(Material block) {this.holdingBlockType = block; return this;}

	/**
	 * 
	 * 
	 * @return Integer Amount of ticks the supporting block will last before removal
	 */
	public int getBlockTime() {return blockTime;}
	
	/**
	 * 
	 * 
	 * @param blockTime - Time before the supporting block is removed
	 * @return HookShotAbility - This instance used for chaining
	 */
	public HookShotAbility setBlockTime(int blockTime) {this.blockTime = blockTime; return this;}

	/**
	 * 
	 * 
	 * @param player - Player used for the ability (Usually the shooter is a projectile shoot event)
	 * @param hook - Arrow used for the ability (Usually the projectile from a projectile shoot event)
	 */
	public HookShotAbility(Player player, Arrow hook){
		super(ABILITY_NAME, player);
		this.hook = hook;
	}

	/**
	 * 
	 * 
	 * @return Boolean - If ability successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Arrow h = getHook();
		Block hBlock = h.getLocation().getBlock();
		Player p = getPlayer();
		
		if (p.getLocation().distance(hBlock.getLocation()) > getMaxDistance())
			return false;
		
		if (hBlock.getType() != Material.AIR)
			return false;
		
		if (hBlock.getRelative(0, 1, 0).getType() != Material.AIR || hBlock.getRelative(0, 2, 0).getType() != Material.AIR)
				return false;
		
		HookShotAbility.hookArrows.add(getHook());
		
		return true;
	}

	/**
	 * 
	 * 
	 * @return Boolean - If the execute() method for this instance will be able to start
	 */
	public boolean mayExecute() {
		if (getPlayer() == null || getHook() == null)
			return false;
		
		return true;
	}
	

}