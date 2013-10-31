package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class SoulStealAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "SoulSteal";
	private LivingEntity target;
	private int duration;
	
	/**
	 * 
	 * 
	 * @return LivingEntity - The Target set for this ability
	 */
	public LivingEntity getE() {return target;}
	
	/**
	 * 
	 * 
	 * @param e - The target to set for this ability
	 * @return SoulStealAbility - This instance, used for chaining
	 */
	public SoulStealAbility setE(LivingEntity e) {this.target = e; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer - The currently set duration for the blindness effect
	 */
	public int getDuration() {return duration;}
	
	/**
	 * 
	 * 
	 * @param duration - The duration is ticks for the blindness effect to last
	 * @return SoulStealAbility - This instance, used for chaining
	 */
	public SoulStealAbility setDuration(int duration) {this.duration = duration; return this;}
	
	/**
	 * 
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public SoulStealAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * 
	 * 
	 * @return Boolean - If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean - If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		if (getPlayer() == null)
			return false;
		if (getE() == null)
			return false;
		
		return true;
	}

	
}
