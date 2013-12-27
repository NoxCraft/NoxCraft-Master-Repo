package com.noxpvp.mmo.abilities.targeted;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class ShadowStepAbility extends BaseTargetedPlayerAbility{
	
	public static final String PERM_NODE = "shadow-step";
	public static final String ABILITY_NAME = "Shadow Step";
	private int range;
	
	/**
	 * 
	 * 
	 * @return Integer the currently set range for the ability execution
	 */
	public int getRange() {return range;}
	
	/**
	 * 
	 * 
	 * @param range The range that the target can be from the ability user
	 * @return ShadowStepAbility This instance, used for chaining
	 */
	public ShadowStepAbility setRange(int range) {this.range = range; return this;}
	
	/**
	 * 
	 * 
	 * @param player the Player type user for this ability instance
	 */
	public ShadowStepAbility(Player player){
		super(ABILITY_NAME, player, NoxMMO.getInstance().getPlayerManager().getMMOPlayer(player).getTarget());
		
		this.range = 10;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ability has executed successfullty
	 * */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		if (getDistance() > range)
			return false;
		
		Location targetLoc = getTarget().getLocation();
		Vector facing = targetLoc.getDirection().setY(0).multiply(-1);
		Location loc = targetLoc.toVector().add(facing).toLocation(targetLoc.getWorld());
		loc.setPitch(0);
		loc.setYaw(targetLoc.getYaw());
		
		
		Block b = loc.getBlock();
		if (!(!b.getType().isSolid() || b.getRelative(BlockFace.UP).getType().isSolid())) {
				return false;
		}
		
		getPlayer().teleport(loc);
		
		return true;
	}
	
}
