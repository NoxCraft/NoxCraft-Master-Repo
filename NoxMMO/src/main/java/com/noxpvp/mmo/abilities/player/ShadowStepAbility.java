package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class ShadowStepAbility extends BasePlayerAbility{
	
	public final static String PERM_NODE = "shadow-step";
	private final static String ABILITY_NAME = "Shadow Step";
	private Entity target;
	private int range;
	
	/**
	 * 
	 * 
	 * @return Integer - the currently set range for the ability execution
	 */
	public int getRange() {return range;}
	
	/**
	 * 
	 * 
	 * @param range - The range that the target can be from the ability user
	 * @return ShadowStepAbility - This instance, used for chaining
	 */
	public ShadowStepAbility setRange(int range) {this.range = range; return this;}
	
	/**
	 * 
	 * 
	 * @return Entity - The currently set target for this ability instance
	 */
	public Entity getTarget() {return target;}
	
	/**
	 * 
	 * 
	 * @param target - The Entity Type target that this ability instance should be targeted at
	 * @return ShadowStepAbility - This instance, used for chaining
	 */
	public ShadowStepAbility setTarget(Entity target) {this.target = target; return this;}
	
	/**
	 * 
	 * 
	 * @param player - the Player type user for this ability instance
	 */
	public ShadowStepAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * 
	 * 
	 * @return Boolean - If the ability has executed successfullty
	 * */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player player = getPlayer();
		Entity target = getTarget();
		
		Location targetLoc = target.getLocation();
		Vector facing = targetLoc.getDirection().setY(0).multiply(-1);
		Location loc = targetLoc.toVector().add(facing).toLocation(targetLoc.getWorld());
		loc.setPitch(0);
		loc.setYaw(targetLoc.getYaw());
		
		
		Block b = loc.getBlock();
		if (!(!b.getType().isSolid() || b.getRelative(BlockFace.UP).getType().isSolid())) {
				return false;
		}
		
		player.teleport(loc);
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean - If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}

	
}
