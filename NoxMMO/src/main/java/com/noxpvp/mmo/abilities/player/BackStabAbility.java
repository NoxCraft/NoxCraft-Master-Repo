package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;

/**
 * @author NoxPVP
 *
 */
public class BackStabAbility extends BasePlayerAbility implements PassiveAbility{
	
	public static final String PERM_NODE = "backstab";
	private static final String ABILITY_NAME = "BackStab";
	private LivingEntity target;
	private float damagePercent;
	private double Damage;
	private double accuracy = 20;
	private float chancePercent = 50;
	
	/**
	 * 
	 * 
	 * @return double the currently set accuracy required for being behind the target
	 */
	public double getAccuracy() {return accuracy;}
	
	/**
	 * 
	 * 
	 * @param accuracy double value in degrees of accuracy required. 0 = exactly behind, 20 (Default) = 20 degrees to either side of target
	 * @return BackStabAbility This instance, used for chaining
	 */
	public BackStabAbility setAccuracy(double accuracy) {this.accuracy = accuracy; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer The current set chance of backstab success
	 */
	public float getChancePercent() {return chancePercent;}
	
	/**
	 * 
	 * 
	 * @param chancePercent The percent chance of a successful backstab
	 * @return BackStabAbility This instance, used for chaining
	 */
	public BackStabAbility setChancePercent(float chancePercent) {this.chancePercent = chancePercent; return this;}
	
	/**
	 * 
	 * 
	 * @return Entity The currently set target for this ability
	 */
	public LivingEntity getTarget() {return target;}
	
	/**
	 * 
	 * 
	 * @param target The LivingEntity type target for this ability instance
	 * @return BackStabAbility This instance, used for chaining
	 */
	public BackStabAbility setTarget(LivingEntity target) {this.target = target; return this;}
	
	/**
	 * 
	 * 
	 * @return double The currently set damage percent. 100% = normal damage.
	 */
	public float getDamagePercent() {return damagePercent;}

	/**
	 * 
	 * 
	 * @param damagePercent double percent value for damage modifier. 100% = normal damage
	 */
	public void setDamagePercent(float damagePercent) {this.damagePercent = damagePercent;}

	/**
	 * 
	 * 
	 * @return double Currently set initial damage value
	 */
	public double getDamage() {return Damage;}

	/**
	 * 
	 * 
	 * @param damage double amount from initial damage event
	 */
	public void setDamage(double damage) {Damage = damage;}

	/**
	 * 
	 * 
	 * @param player The Player type user for this ability instance
	 */
	public BackStabAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		LivingEntity t = getTarget();
		Player p = getPlayer();
		
		double tYaw = t.getLocation().getYaw();
		double pYaw = p.getLocation().getYaw();
		
		if (!(pYaw <= (tYaw + accuracy)) && (pYaw >= (tYaw - accuracy)) && (Math.random() < getChancePercent()))
			return false;
		
		t.damage(getDamagePercent() * getDamage(), p);
					
		return true;
	}
	
}
