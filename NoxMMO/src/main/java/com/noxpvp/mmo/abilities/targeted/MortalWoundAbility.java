package com.noxpvp.mmo.abilities.targeted;

import java.util.Arrays;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class MortalWoundAbility extends BaseTargetedPlayerAbility{
	
	public static final String ABILITY_NAME = "Mortal Wound";
	public static final String PERM_NODE = "mortal-wound";
	
	private double range;
	private double damage;
	private int duration;
	private int amplifier;
	
	/**
	 * Gets the range set for this ability
	 * 
	 * @return double Range
	 */
	public double getRange() {return range;}

	/**
	 * Sets the range for this ability
	 * 
	 * @param range Range
	 * @return MortalWoundAbility This instance
	 */
	public MortalWoundAbility setRange(double range) {this.range = range; return this;}

	/**
	 * Gets the damage set for this ability
	 * 
	 * @return double damage
	 */
	public double getDamage() {return damage;}

	/**
	 * Sets the damage for this ability
	 * 
	 * @param damage The damage
	 * @return MortalWoundAbility This instance
	 */
	public MortalWoundAbility setDamage(double damage) {this.damage = damage; return this;}

	/**
	 * Gets the duration in ticks set for this ability
	 * 
	 * @return Integer Tick length duration
	 */
	public int getDuration() {return duration;}

	/**
	 * Sets the duration in ticks set for this ability
	 * 
	 * @param duration Duration in ticks
	 * @return MortalWoundAbility This instance
	 */
	public MortalWoundAbility setDuration(int duration) {this.duration = duration; return this;}

	/**
	 * Gets the amplifier for the slowness and poison in this ability
	 * 
	 * @return Integer Amplifier
	 */
	public int getAmplifier() {return amplifier;}

	/**
	 * Sets the amplifier for the slowness and poison effects in this ability
	 * 
	 * @param amplifier The amplifier
	 * @return MortalWoundAbility This instance
	 */
	public MortalWoundAbility setAmplifier(int amplifier) {this.amplifier = amplifier; return this;}

	public MortalWoundAbility(Player player){
		super(ABILITY_NAME, player, NoxMMO.getInstance().getPlayerManager().getMMOPlayer(player).getTarget());
		
		this.range = 5;
		this.damage = 6;
		this.duration = (20 * 4);
		this.amplifier = 2;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		if (getDistance() > range) return false;
		
		LivingEntity t = getTarget();
		Player p = getPlayer();
		
		t.damage(damage, p);
		return t.addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.POISON, duration, amplifier),
				new PotionEffect(PotionEffectType.SLOW, duration, amplifier)));
	}
	
}
