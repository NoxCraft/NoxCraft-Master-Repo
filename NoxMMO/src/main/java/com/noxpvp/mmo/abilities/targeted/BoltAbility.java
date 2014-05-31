/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.abilities.targeted;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class BoltAbility extends BaseTargetedPlayerAbility{
	
	private final static String ABILITY_NAME = "Bolt";
	public final static String PERM_NODE = "bolt";
	
	private double range;
	private double damage;
	
	/**
	 * Sets the range of this ability - Defaults to 12 
	 * 
	 * @param range - The max distance away from the player a target can be
	 * @return BoltAbility - This instance
	 */
	public BoltAbility setRange(double range) {this.range = range; return this;}
	
	/**
	 * The currently set max distance away from the player a target can be
	 * 
	 * @return double - The currently set max range of this ability instance
	 */
	public double getRange() {return range;}

	/**
	 * Gets the currently set amount of damage to inflict on the target after they have been bolted
	 * 
	 * @return double - The currently set amount of damage to inflict on the target after they have been bolted
	 */
	public double getDamage() {return damage;}

	/**
	 * Sets the amount of damage to inflict of the target after they have been bolted
	 * 
	 * @param damage - the amount of damage to inflict
	 * @return BoltAbility - This instance
	 */
	public BoltAbility setDamage(double damage) {this.damage = damage; return this;}
	
	/**
	 * Constructs a new BoltAbility instance with the specified player as the ability user
	 * 
	 * @param player - The player to use as the abilities user
	 */
	public BoltAbility(Player player){
		super(ABILITY_NAME, player, PlayerManager.getInstance().getPlayer(player).getTarget());
		
		this.range = 12;
	}
	
	/**
	 * Constructs a new BoltAbility instance with the specified player as the ability user and specified range
	 * 
	 * @param player - The player to use as the abilities user
	 * @param range - The max distance away from the user that a target can be
	 */
	public BoltAbility(Player player, double range){
		super(ABILITY_NAME, player, PlayerManager.getInstance().getPlayer(player).getTarget());
		
		this.range = range;
	}

	public boolean execute(){
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		LivingEntity t = getTarget();
		
		if (getDistance() > range) return false;
		
		t.getWorld().strikeLightningEffect(t.getLocation());
		t.damage(damage, p);
		
		return true;
	}
	
}
