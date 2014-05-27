package com.noxpvp.mmo.abilities.targeted;

import com.noxpvp.mmo.abilities.PVPAbility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class BoltPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "bolt";
	private static final String ABILITY_NAME = "Bolt";

	/**
	 * Constructs a new BoltAbility instance with the specified player as the ability user
	 *
	 * @param player - The player to use as the abilities user
	 */
	public BoltPlayerAbility(Player player) {
		this(player, 10);
	}

	/**
	 * Constructs a new BoltAbility instance with the specified player as the ability user and specified range
	 *
	 * @param player - The player to use as the abilities user
	 * @param range  - The max distance away from the user that a target can be
	 */
	public BoltPlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

		setDamage(8);
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();
		LivingEntity t = getTarget();

		t.getWorld().strikeLightningEffect(t.getLocation());
		t.damage(getDamage(), p);

		return new AbilityResult(this, true);
	}

}
