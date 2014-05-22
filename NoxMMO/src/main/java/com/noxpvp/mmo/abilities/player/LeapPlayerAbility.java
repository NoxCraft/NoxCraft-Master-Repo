package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;

public class LeapPlayerAbility extends BaseEntityAbility {

	public static final String ABILITY_NAME = "Leap";
	public static final String PERM_NODE = "leap";
	
	@Override
	public String getDescription() {
		return "You leap forward in your current direction";
	}
	
	private double multiplier;
	
	/**
	 * Gets the current multiplier for the ability's forward velocity
	 * 
	 * @return double The forward multiplier
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * Sets the multiplier for this ability's forward velocity
	 * 
	 * @param forwardMultiplier The multiplier to set
	 * @return LeapAbility This instance
	 */
	public LeapPlayerAbility setMultiplier(double forwardMultiplier) {
		this.multiplier = forwardMultiplier;
		return this;
		}

	public LeapPlayerAbility(Entity ent, double multiplier) {
		super(ABILITY_NAME, ent);
		this.multiplier = multiplier;
	}
	
	public LeapPlayerAbility(Entity ent) {
		this(ent, 2D);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Entity e = getEntity();
		Location eLoc = e.getLocation();
		Vector newVelocity = eLoc.getDirection();
		newVelocity.multiply(multiplier);
		
		// if going up a reasonable amount
		if (newVelocity.getY() > .75) {
			new ParticleRunner(ParticleType.cloud, eLoc.clone().add(0, 2, 0), true, 0, 50, 1).start(0);
			new ShockWaveAnimation(eLoc, 1, 2, true).start(0);
		}
	
		//reset fall distance on use
		e.setFallDistance(0);
		e.setVelocity(newVelocity);		
		return true;
	}

}
