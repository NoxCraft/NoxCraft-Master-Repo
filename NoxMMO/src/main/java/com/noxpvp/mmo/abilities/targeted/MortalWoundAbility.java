package com.noxpvp.mmo.abilities.targeted;

import java.util.Arrays;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;

public class MortalWoundAbility extends BaseTargetedPlayerAbility implements PVPAbility {
	
	public static final String ABILITY_NAME = "Mortal Wound";
	public static final String PERM_NODE = "mortal-wound";
	
	@Override
	public String getDescription() {
		return "Bypass all your targets powers and hit them as the mortal they are, causing slowness and posion";
	}
	
	private int duration;
	private int amplifier;

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

	public MortalWoundAbility(Player player) {
		this(player, 10);
	}
	
	public MortalWoundAbility(Player player, double range){
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
		
		setDamage(8);
		this.duration = (20 * 4);
		this.amplifier = 2;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		LivingEntity t = getTarget();
		Player p = getPlayer();
		
		t.damage(getDamage(), p);
		
		return t.addPotionEffects(Arrays.asList(
				new PotionEffect(PotionEffectType.POISON, duration, amplifier),
				new PotionEffect(PotionEffectType.SLOW, duration, amplifier)));
	}
	
}
