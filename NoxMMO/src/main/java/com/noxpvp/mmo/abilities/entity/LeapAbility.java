package com.noxpvp.mmo.abilities.entity;

import java.util.Arrays;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.runnables.EffectsRunnable;

public class LeapAbility extends BaseEntityAbility {

	public static final String ABILITY_NAME = "Leap";
	public static final String PERM_NODE = "leap";
	
	private double forwardMultiplier;
	private double heightVelo;
	
	/**
	 * Gets the current multiplier for the ability's forward velocity
	 * 
	 * @return double The forward multiplier
	 */
	public double getForwardMultiplier() {return forwardMultiplier;}

	/**
	 * Sets the multiplier for this ability's forward velocity
	 * 
	 * @param forwardMultiplier The multiplier to set
	 * @return LeapAbility This instance
	 */
	public LeapAbility setForwardMultiplier(double forwardMultiplier) {this.forwardMultiplier = forwardMultiplier; return this;}

	/**
	 * Gets the current set height velocity
	 * 
	 * @return LeapAbility This instance
	 */
	public double getHeightVelo() {return heightVelo;}

	/**
	 * Sets the height velocity for this ability
	 * 
	 * @param heightVelo The height velocity
	 * @return LeapAbility This instance
	 */
	public LeapAbility setHeightVelo(double heightVelo) {this.heightVelo = heightVelo; return this;}

	public LeapAbility(Entity ent, double heightVelo, double forwardMultiplier)
	{
		super(ABILITY_NAME, ent);
		this.heightVelo = heightVelo;
		this.forwardMultiplier = forwardMultiplier;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Entity e = getEntity();
		
		Vector newVelocity = e.getLocation().getDirection();
		
		newVelocity.setY(0).multiply(forwardMultiplier).setY(heightVelo);
		
		new EffectsRunnable(Arrays.asList("blockcrack_"+ e.getLocation().getBlock().getRelative(BlockFace.DOWN).getTypeId() +"_0"), false, e.getLocation(), 0, 50, 1, null).runTask(NoxMMO.getInstance());
		
		e.setVelocity(newVelocity);		
		return true;
	}

	public boolean mayExecute() {
		if (isValid() && getEntity() instanceof Player)
			return VaultAdapter.permission.has((Player)getEntity(), NoxMMO.PERM_NODE + ".abilities."+PERM_NODE);
		return isValid();
	}

}
