package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class ShadowStepAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Shadow Step";
	private Entity target;
	private int range;
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Integer - the currently set range for the ability execution
	 */
	public int getRange() {return range;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param range - The range that the target can be from the ability user
	 * @return ShadowStepAbility - This instance, used for chaining
	 */
	public ShadowStepAbility setRange(int range) {this.range = range; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Entity - The currently set target for this ability instance
	 */
	public Entity getTarget() {return target;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param target - The Entity Type target that this ability instance should be targeted at
	 * @return ShadowStepAbility - This instance, used for chaining
	 */
	public ShadowStepAbility setTarget(Entity target) {this.target = target; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param player - the Player type user for this ability instance
	 */
	public ShadowStepAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * @author Connor Stone
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
	 * @author Connor Stone
	 * 
	 * @return Boolean - If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}

	
}
