package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class ShadowStepAbility extends BasePlayerAbility{
	
	public final static String PERM_NODE = "shadow-step";
	private final static String ABILITY_NAME = "Shadow Step";
	private Entity target;
	private int range;
	
	public int getRange() {return range;}
	public ShadowStepAbility setRange(int range) {this.range = range; return this;}
	
	public Entity getTarget() {return target;}
	public ShadowStepAbility setTarget(Entity target) {this.target = target; return this;}
	
	public ShadowStepAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
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
	
	public boolean mayExecute() {
		return getPlayer() != null;
	}

	
}
