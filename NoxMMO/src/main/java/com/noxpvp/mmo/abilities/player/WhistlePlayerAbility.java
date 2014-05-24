package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class WhistlePlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Whistle";
	public static final String PERM_NODE = "whistle";

	private int range;

	public WhistlePlayerAbility(Player player) {
		super(ABILITY_NAME, player);
		this.range = 15;
	}

	/**
	 * @return Integer The currently set range for this ability instance (Default is 15)
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range The range to check for nearby targets  (Default is 15)
	 * @return WhistleAbility This instance, used for chaining
	 */
	public WhistlePlayerAbility setRange(int range) {
		this.range = range;
		return this;
	}

	/**
	 * @return boolean If the ability has executed successfully
	 */
	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();

		for (Entity it : p.getNearbyEntities(range, range, range)) {
			if (!(it instanceof Wolf)) continue;
			if (((Wolf) it).getOwner() != p) continue;

			Wolf n = (Wolf) it;

			if (n.isSitting()) n.setSitting(false);
			else if (!n.isSitting()) n.setSitting(true);
		}

		return new AbilityResult(this, false);
	}

}
