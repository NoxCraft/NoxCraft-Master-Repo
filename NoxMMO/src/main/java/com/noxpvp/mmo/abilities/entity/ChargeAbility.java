package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class ChargeAbility extends BaseEntityAbility {

	public static final String ABILITY_NAME = "Charge";
	public static final String PERM_NODE = "charge";
	
	@Override
	public String getDescription() {
		return "You charge forward with great speed";
	}
	
	private double forwardMultiplier;
	
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
	 * @return ChargeAbility This instance
	 */
	public ChargeAbility setForwardMultiplier(double forwardMultiplier) {this.forwardMultiplier = forwardMultiplier; return this;}

	public ChargeAbility(Entity ent, double forwardMultiplier) {
		super(ABILITY_NAME, ent);
		this.forwardMultiplier = forwardMultiplier;
	}
	
	public ChargeAbility(Entity ent) {
		this(ent, 10);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Entity e = getEntity();
		
		Vector newVelocity = e.getLocation().getDirection();
		
		newVelocity.setY(0).multiply(forwardMultiplier);
		
		new ParticleRunner(ParticleType.largesmoke, e, true, 0, 6, 10).start(0, 1);
		
		e.setVelocity(newVelocity);		
		return true;
	}

}
