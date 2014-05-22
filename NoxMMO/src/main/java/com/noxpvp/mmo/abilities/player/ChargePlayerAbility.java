package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class ChargePlayerAbility extends BasePlayerAbility {

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
	public ChargePlayerAbility setForwardMultiplier(double forwardMultiplier) {this.forwardMultiplier = forwardMultiplier; return this;}

	public ChargePlayerAbility(Player p, double forwardMultiplier) {
		super(ABILITY_NAME, p);
		this.forwardMultiplier = forwardMultiplier;
	}
	
	public ChargePlayerAbility(Player p) {
		this(p, 10);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		Vector newVelocity = p.getLocation().getDirection();
		
		newVelocity.setY(0).multiply(forwardMultiplier);
		
		new ParticleRunner(ParticleType.largesmoke, p, true, 0, 6, 10).start(0, 1);
		
		p.setVelocity(newVelocity);		
		return true;
	}

}
