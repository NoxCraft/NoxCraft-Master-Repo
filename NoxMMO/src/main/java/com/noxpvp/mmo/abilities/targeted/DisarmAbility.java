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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class DisarmAbility extends BaseTargetedPlayerAbility{
	
	public static List<LivingEntity> disarmed = new ArrayList<LivingEntity>();
	
	private final static String ABILITY_NAME = "Disarm";
	public static final String PERM_NODE = "disarm";
	
	/**
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public DisarmAbility(Player player){
		super(ABILITY_NAME, player, PlayerManager.getInstance().getPlayer(player).getTarget());
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		DisarmAbility.disarmed.add(getTarget());
		
		return true;
	}
	
}
