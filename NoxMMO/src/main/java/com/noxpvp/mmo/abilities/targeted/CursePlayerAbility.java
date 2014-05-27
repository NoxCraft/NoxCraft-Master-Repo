package com.noxpvp.mmo.abilities.targeted;

import com.noxpvp.mmo.abilities.PVPAbility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class CursePlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "curse";
	private static final String ABILITY_NAME = "Curse";
	private int duration;
	private int lethality;

	public CursePlayerAbility(Player player) {
		this(player, 15);
	}

	public CursePlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

		this.duration = 100;
		this.lethality = 1;
	}

	/**
	 * Gets the currently set duration in ticks of the curse effect.
	 *
	 * @return Integer Duration of the curse effect.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the duration in ticks of the curse effect.
	 *
	 * @param duration The duration of the curse effect.
	 * @return CurseAbility This instance
	 */
	public CursePlayerAbility setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * Gets the currently set amplifier for the curse effect.
	 *
	 * @return Integer Amplifier for the curse effect.
	 */
	public int getLethality() {
		return lethality;
	}

	/**
	 * Sets the amplifier of the curse effect.
	 *
	 * @param lethality The amplifier of the curse effect.
	 * @return CurseAbility This instance
	 */
	public CursePlayerAbility setLethality(int lethality) {
		this.lethality = lethality;
		return this;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		LivingEntity t = getTarget();

		t.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration, lethality));
		t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, lethality));

		new ParticleRunner(ParticleType.angryVillager, t, false, 0, 1, 1).start(0);

		return new AbilityResult(this, true);
	}
}
