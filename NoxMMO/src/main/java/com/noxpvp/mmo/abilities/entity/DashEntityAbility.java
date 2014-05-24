package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

/**
 * @author NoxPVP
 */
public class DashEntityAbility extends BaseEntityAbility {

	public static final String ABILITY_NAME = "Dash";
	public static final String PERM_NODE = "dash";

	private int s = 2;
	private int d = (20 * 6);

	/**
	 * @param entity Entity to apply ability on
	 */
	public DashEntityAbility(LivingEntity entity) {
		super(ABILITY_NAME, entity);
	}

	/**
	 * @return Currently set speed amplifier
	 */
	public int getS() {
		return s;
	}

	/**
	 * @param s Speed effect amplifier (same as minecraft potions)
	 */
	public void setS(int s) {
		this.s = s;
	}

	/**
	 * @return Currently set duration time
	 */
	public int getD() {
		return d;
	}

	/**
	 * @param d Tick amount of speed duration (default is 6 seconds or 20*6)
	 */
	public void setD(int d) {
		this.d = d;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		((LivingEntity) getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, d, s, true));

		return new AbilityResult(this, true);
	}

}
