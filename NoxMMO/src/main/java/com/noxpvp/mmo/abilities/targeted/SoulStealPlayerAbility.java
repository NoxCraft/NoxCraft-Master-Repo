package com.noxpvp.mmo.abilities.targeted;

import com.noxpvp.mmo.abilities.PVPAbility;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

/**
 * @author NoxPVP
 */
public class SoulStealPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "soulsteal";
	public static final String ABILITY_NAME = "SoulSteal";
	private int duration;

	/**
	 * @param player The Player type user for this ability instance
	 */
	public SoulStealPlayerAbility(Player player) {
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
	}

	/**
	 * @return Integer The currently set duration for the blindness effect
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration The duration is ticks for the blindness effect to last
	 * @return SoulStealAbility This instance, used for chaining
	 */
	public SoulStealPlayerAbility setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * @return Boolean If the ability has successfully executed
	 */
	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		getTarget().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));

		return new AbilityResult(this, true);
	}

}
