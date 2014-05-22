package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.runnables.HealRunnable;

/**
 * @author NoxPVP
 *
 */
public class RejuvenationPlayerAbility extends BasePlayerAbility implements IPVPAbility {
	
	public static final String ABILITY_NAME = "Rejuvenation";
	public static final String PERM_NODE = "rejuvenation";
	
	private double healthPerHeal;
	private int heals;
	private int delayBetweenHeals;
	
	/**
	 * 
	 * 
	 * @return Integer The currently set amount of health the player will receive each heal
	 */
	public double getHealthPerHeal() {return healthPerHeal;}
	
	/**
	 * 
	 * 
	 * @param healthPerHeal The amount of health the player should receive every heal
	 * @return RejuvenationAbility This instance, used for chaining
	 */
	public RejuvenationPlayerAbility setHealthPerHeal(double healthPerHeal) {this.healthPerHeal = healthPerHeal; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer The amount of time the player will be healed with the set healthPerHeal
	 */
	public int getHeals() {return heals;}
	
	/**
	 * 
	 * 
	 * @param heals The Integer amount of time a player should be healed
	 * @return RejuvenationAbility this instance, used for chaining
	 */
	public RejuvenationPlayerAbility setHeals(int heals) {this.heals = heals; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer the amount of delay in ticks  between the the set amount of heals
	 */
	public int getDelayBetweenHeals() {return delayBetweenHeals;}
	
	/**
	 * 
	 * 
	 * @param delayBetweenHeals The Integer amount of tick delay between heals
	 * @return RejuvenationAbility This instance, used for chaining
	 */
	public RejuvenationPlayerAbility setDelayBetweenHeals(int delayBetweenHeals) {this.delayBetweenHeals = delayBetweenHeals; return this;}
	
	/**
	 * 
	 * 
	 * Rejuvenation Heal-Over-Time Ability
	 * 
	 * @param player The Player type user for this ability instance
	 */
	public RejuvenationPlayerAbility(Player player){
		super(ABILITY_NAME, player);
		
		this.healthPerHeal = 2;
		this.heals = 6;
		this.delayBetweenHeals = 15;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		new HealRunnable(p, healthPerHeal, heals).runTaskTimer(NoxMMO.getInstance(), 0, delayBetweenHeals);
		
		return true;
	}
	
}