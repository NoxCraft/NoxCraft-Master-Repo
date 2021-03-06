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

package com.noxpvp.mmo.abilities.entity;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class MaliciousBiteAbility extends BaseEntityAbility implements PassiveAbility<EntityDamageByEntityEvent> {
	
	public static final String ABILITY_NAME = "Malicious bite";
	public static final String PERM_NODE = "malicious-bite";

	public MaliciousBiteAbility(Entity entity) {
		super(ABILITY_NAME, entity);
	}
	
	public boolean execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return false;
		
		if (!(getEntity() instanceof Tameable)) return false;
		
		AnimalTamer a = ((Tameable) getEntity()).getOwner();
		
		if (a == null || !(a instanceof Player)) return false;
		
		Player o = (Player) a;
		
		PlayerClass pClass = PlayerManager.getInstance().getPlayer(o).getPrimaryClass();
		
		return RandomUtils.nextFloat() < (pClass.getCurrentTierLevel() * pClass.getLevel()) / 1000;
	}
	
	public boolean execute() {
			return true;
	}

}
