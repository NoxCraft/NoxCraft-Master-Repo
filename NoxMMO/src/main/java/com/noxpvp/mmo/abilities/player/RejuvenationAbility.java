package com.noxpvp.mmo.abilities.player;

import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.EntityEffectRunnable;
import com.noxpvp.mmo.runnables.HealRunnable;

public class RejuvenationAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Rejuvenation";
	private double healthPerHeal = 1;
	private int heals = 6;
	private int delayBetweenHeals = 15;
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Integer - The currently set amount of health the player will receive each heal
	 */
	public double getHealthPerHeal() {return healthPerHeal;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param healthPerHeal - The amount of health the player should receive every heal
	 * @return RejuvenationAbility - This instance, used for chaining
	 */
	public RejuvenationAbility setHealthPerHeal(double healthPerHeal) {this.healthPerHeal = healthPerHeal; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Integer - The amount of time the player will be healed with the set healthPerHeal
	 */
	public int getHeals() {return heals;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param heals - The Integer amount of time a player should be healed
	 * @return RejuvenationAbility - this instance, used for chaining
	 */
	public RejuvenationAbility setHeals(int heals) {this.heals = heals; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Integer - the amount of delay in ticks  between the the set amount of heals
	 */
	public int getDelayBetweenHeals() {return delayBetweenHeals;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param delayBetweenHeals - The Integer amount of tick delay between heals
	 * @return RejuvenationAbility - This instance, used for chaining
	 */
	public RejuvenationAbility setDelayBetweenHeals(int delayBetweenHeals) {this.delayBetweenHeals = delayBetweenHeals; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * Rejuvenation Heal-Over-Time Ability
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public RejuvenationAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Boolean - If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		NoxMMO instance = NoxMMO.getInstance();
		
		HealRunnable rejuvenation = new HealRunnable(getPlayer(), healthPerHeal, heals);
		EntityEffectRunnable hearts = new EntityEffectRunnable(getPlayer(), EntityEffect.WOLF_HEARTS, heals);
		
		rejuvenation.runTaskTimer(instance, 0, delayBetweenHeals);
		hearts.runTaskTimer(instance, 0, delayBetweenHeals);
		
		return true;
	}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Boolean - If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}
	
	
}
