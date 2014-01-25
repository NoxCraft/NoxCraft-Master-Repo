package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

/**
 * @author NoxPVP
 *
 */
public class BackStabAbility extends BasePlayerAbility implements PassiveAbility<EntityDamageByEntityEvent> {
	
	public static final String PERM_NODE = "backstab";
	public static final String ABILITY_NAME = "BackStab";
	private LivingEntity target;
	private float damagePercent;
	private double Damage;
	private double accuracy = 20;
	
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
	
	public boolean execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return false;
		
		LivingEntity t = getTarget();
		Player p = getPlayer();
		
		Location pLoc = p.getLocation();
		Location tLoc = t.getLocation();		
		double tYaw = tLoc.getYaw();
		double pYaw = pLoc.getYaw();
		
		if (!(pYaw <= (tYaw + accuracy)) && (pYaw >= (tYaw - accuracy)))
			return false;
		
		MMOPlayer player = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(p);
		if (player == null)
			return false;
		
		PlayerClass clazz = player.getMainPlayerClass();
		
		float chance = (clazz.getLevel() + clazz.getTotalLevels()) / 10;//up to 40% at max 400 total levels
		if ((Math.random() * 100) > chance)
			return false;
					
		if (pLoc.distance(tLoc) < .35)//prevent if inside the target
			return false;
		
		event.setDamage(event.getDamage() + getDamage());
		
		return true;
	}
	
	public boolean execute() {
		return true;
	}
	
}
