package com.noxpvp.mmo.abilities.targeted;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;

/**
 * @author NoxPVP
 */
public class SoulStealPlayerAbility extends BaseTargetedPlayerAbility implements IPVPAbility {

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
	public boolean execute() {
		if (!mayExecute())
			return false;

		getTarget().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));

		return true;
	}

}
