package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class ChargePlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Charge";
	public static final String PERM_NODE = "charge";
	
	private double forwardMultiplier;

	public ChargePlayerAbility(Player p, double forwardMultiplier) {
		super(ABILITY_NAME, p);
		this.forwardMultiplier = forwardMultiplier;
		
		setCD(15);
	}

	public ChargePlayerAbility(Player p) {
		this(p, 10);
	}

	@Override
	public String getDescription() {
		return "You charge forward with great speed";
	}

	/**
	 * Gets the current multiplier for the ability's forward velocity
	 *
	 * @return double The forward multiplier
	 */
	public double getForwardMultiplier() {
		return forwardMultiplier;
	}

	/**
	 * Sets the multiplier for this ability's forward velocity
	 *
	 * @param forwardMultiplier The multiplier to set
	 * @return ChargeAbility This instance
	 */
	public ChargePlayerAbility setForwardMultiplier(double forwardMultiplier) {
		this.forwardMultiplier = forwardMultiplier;
		return this;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();

		Vector newVelocity = p.getLocation().getDirection();

		newVelocity.setY(0).multiply(forwardMultiplier);

		new ParticleRunner(ParticleType.largesmoke, p, true, 0, 6, 10).start(0, 1);

		p.setVelocity(newVelocity);
		return new AbilityResult(this, true);
	}

}
