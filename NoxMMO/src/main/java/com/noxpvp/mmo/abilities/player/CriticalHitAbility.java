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

package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class CriticalHitAbility extends BasePlayerAbility implements PassiveAbility<EntityDamageByEntityEvent> {

	public static final String PERM_NODE = "Critical Hit";
	public static final String ABILITY_NAME = "critical-hit";
	
	private PlayerManager pm;
	
	public CriticalHitAbility(Player p) {
		super(ABILITY_NAME, p);
		
		this.pm = PlayerManager.getInstance();
	}
	
	public boolean execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return false;
		
		Player playerAttacker = (Player) ((event.getDamager() instanceof Player)? event.getDamager(): null);
		String itemName = playerAttacker.getItemInHand().getType().name().toUpperCase();
		
		if (!itemName.contains("SWORD") && !itemName.contains("AXE"))
			return false;
		
		if (playerAttacker == null || !playerAttacker.equals(getPlayer()))
			return false;
		
		MMOPlayer player = pm.getPlayer(getPlayer());
		
		if (player == null)
			return false;
		
		PlayerClass clazz = player.getPrimaryClass();
		
		double damage = (clazz.getLevel() + clazz.getTotalLevel()) / 75;
		if ((Math.random() * 100) > (damage * 45)) return false;
		
		event.setDamage(damage);
		return true;
	}
	
	/**
	 * Always Returns True Due To Being Passive!
	 */
	public boolean execute() { 
		return true; //Passive
	}

}
