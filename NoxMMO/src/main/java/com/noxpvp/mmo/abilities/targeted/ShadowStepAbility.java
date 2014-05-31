/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.abilities.targeted;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
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
		super(ABILITY_NAME, player, PlayerManager.getInstance().getPlayer(player).getTarget());
		
		this.range = 10;
	}

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
