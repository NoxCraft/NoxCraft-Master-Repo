package com.noxpvp.mmo.abilities.targeted;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class SoothePlayerAbility extends BaseTargetedPlayerAbility {

	public static final String ABILITY_NAME = "Soothe";
	public static final String PERM_NODE = "soothe";

	private double healAmount;

	/**
	 * Constructs a new Soothe Ability with the provided player as the user
	 *
	 * @param player The ability's user
	 */
	public SoothePlayerAbility(Player player) {
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

		this.healAmount = 8;
	}

	/**
	 * Gets the amount of health that will be given to the target
	 *
	 * @return Double The amount to heal the target
	 */
	public double getHealAmount() {
		return healAmount;
	}

	/**
	 * Sets the amount of health to give to the target
	 *
	 * @param healAmount The amount to heal the target
	 * @return SootheAbility This instance
	 */
	public SoothePlayerAbility setHealAmount(double healAmount) {
		this.healAmount = healAmount;
		return this;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		LivingEntity t = getTarget();
		double ha = t.getHealth() + getHealAmount();

		t.setHealth(ha > t.getMaxHealth() ? t.getMaxHealth() : ha);

		return new AbilityResult(this, true);
	}

}
