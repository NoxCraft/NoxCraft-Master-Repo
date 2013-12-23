package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

/**
 * @author NoxPVP
 *
 */
public class DashAbility extends BaseEntityAbility{
	
	private static final String ABILITY_NAME = "Dash";
	public static final String PERM_NODE = "dash";
	
	private int s = 2;
	private int d = (20 * 6);
	
	/**
	 * 
	 * 
	 * @return Currently set speed amplifier
	 */
	public int getS() {return s;}
	
	/**
	 * 
	 * 
	 * @param s Speed effect amplifier (same as minecraft potions)
	 */
	public void setS(int s) {this.s = s;}
	
	/**
	 * 
	 * 
	 * @return Currently set duration time
	 */
	public int getD() {return d;}
	
	/**
	 * 
	 * 
	 * @param d Tick amount of speed duration (default is 6 seconds or 20*6)
	 */
	public void setD(int d) {this.d = d;}
	
	/**
	 * 
	 * 
	 * @param entity Entity to apply ability on
	 */
	public DashAbility(LivingEntity entity){
		super(ABILITY_NAME, entity);
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		((LivingEntity) getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, d, s, true));
		
		return true;
	}
	
}
