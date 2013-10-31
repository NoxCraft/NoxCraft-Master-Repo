package com.noxpvp.mmo.abilities.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.BlockTimerRunnable;

public class HookShotAbility extends BasePlayerAbility{
	
	public final static String PERM_NODE = "hookshot";
	private final static String ABILITY_NAME = "Hook Shot";
	private Arrow hook;
	private int maxDistance;
	private int blockTime;
	private Material holdingBlock;
	
	public Arrow getHook() {return hook;}
	public HookShotAbility setHook(Arrow hook) {this.hook = hook; return this;}

	public int getMaxDistance() {return maxDistance;}
	public HookShotAbility setMaxDistance(int maxDistance) {this.maxDistance = maxDistance; return this;}

	public Material getHoldingBlock() {return holdingBlock;}
	public HookShotAbility setHoldingBlock(Material block) {this.holdingBlock = block; return this;}

	public int getBlockTime() {return blockTime;}
	public HookShotAbility setBlockTime(int blockTime) {this.blockTime = blockTime; return this;}

	public HookShotAbility(Player player, Arrow hook){
		super(ABILITY_NAME, player);
		this.hook = hook;
	}

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
		
		hBlock.setType(getHoldingBlock());
		p.teleport(hBlock.getLocation());
		
		BlockTimerRunnable removeBlock = new BlockTimerRunnable(hBlock, Material.AIR, getHoldingBlock());
		removeBlock.runTaskLater(NoxMMO.getInstance(), getBlockTime());
		
		return true;
	}

	public boolean mayExecute() {
		if (getPlayer() == null || getHook() == null)
			return false;
		
		return true;
	}
	

}