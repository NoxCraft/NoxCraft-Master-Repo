package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.utils.InventoryUtils;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.BlockTimerRunnable;

/**
 * @author NoxPVP
 *
 */
public class HookShotAbility extends BasePlayerAbility{
	
	public static final String PERM_NODE = "hookshot";
	private static final String ABILITY_NAME = "Hook Shot";
	
	public static Map<String, Arrow> hookArrows = new HashMap<String, Arrow>();
	
	private static ItemStack pullRegent = new ItemStack(Material.STRING, 1);
	private static ItemStack shootRegent = new ItemStack(Material.ARROW, 1);
	
	private double maxDistance;
	private int blockTime;
	private Material holdingBlockType;

	/**
	 * 
	 * 
	 * @return Integer Max distance set for ability execution <br/> returns null is setMaxDistance() has not been used
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
	 * @return Material Type of block used to support player
	 */
	public Material getHoldingBlockType() {return holdingBlockType;}
	
	/**
	 * 
	 * 
	 * @param block Material type that should be used to support the player
	 * @return HookShotAbility This instance used for chaining
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
	 * @param blockTime Time before the supporting block is removed
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotAbility setBlockTime(int blockTime) {this.blockTime = blockTime; return this;}

	/**
	 * 
	 * 
	 * @param player Player used for the ability (Usually the shooter is a projectile shoot event)
	 * @param hook Arrow used for the ability (Usually the projectile from a projectile shoot event)
	 */
	public HookShotAbility(Player player){
		super(ABILITY_NAME, player);
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		String pName = p.getName();
		Inventory inv = p.getInventory();
		
		if (HookShotAbility.hookArrows.containsKey(pName)){
			Arrow a = HookShotAbility.hookArrows.get(pName);
			Block hBlock = a.getLocation().getBlock();
			
			
			if (!InventoryUtils.hasItems(inv, pullRegent))
				return false;
			if (!p.hasLineOfSight(a))
				return false;
			if (hBlock.getType() != Material.AIR || hBlock.getRelative(0, 1, 0).getType() != Material.AIR || hBlock.getRelative(0, 2, 0).getType() != Material.AIR){
				HookShotAbility.hookArrows.remove(pName);
				a.remove();
				return false;
			}
			
			inv.removeItem(pullRegent);
			
			hBlock.setType(holdingBlockType);
			BlockTimerRunnable remover = new BlockTimerRunnable(hBlock, Material.AIR, holdingBlockType);
			remover.runTaskLater(NoxMMO.getInstance(), blockTime);
			
			p.teleport(hBlock.getRelative(0, 1, 0).getLocation(), TeleportCause.PLUGIN);
			a.remove();
		} else {
			if (!InventoryUtils.hasItems(inv, shootRegent))
				return false;
			
			inv.removeItem(shootRegent);
			
			Arrow a = p.launchProjectile(Arrow.class);
			a.setBounce(true);
			a.setVelocity(p.getLocation().getDirection());
			
			HookShotAbility.hookArrows.put(pName, a);
		}
		return true;
	}

}