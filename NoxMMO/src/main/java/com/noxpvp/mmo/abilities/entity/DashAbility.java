package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class DashAbility extends BaseEntityAbility{
	
	private final static String ABILITY_NAME = "Dash";
	private final static String PERM_NODE = "dash";
	
	private int s = 2;
	private int d = (20 * 6);
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Currently set speed amplifier
	 */
	public int getS() {return s;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param s Speed effect amplifier (same as minecraft potions)
	 */
	public void setS(int s) {this.s = s;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Currently set duration time
	 */
	public int getD() {return d;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param d Tick amount of speed duration (default is 6 seconds or 20*6)
	 */
	public void setD(int d) {this.d = d;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param entity - Entity to apply ability on
	 */
	public DashAbility(LivingEntity entity){
		super(ABILITY_NAME, entity);
	}

	/**
	 * @author Connor Stone
	 * 
	 * @return boolean - Ability success
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		((LivingEntity) getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, d, s, true));
		
		return true;
	}

	/**
	 * @author Connor Stone
	 * 
	 * @return boolean - If the execute method from this instance is able to start
	 */
	public boolean mayExecute() {
		return getEntity() != null;
	}
}
