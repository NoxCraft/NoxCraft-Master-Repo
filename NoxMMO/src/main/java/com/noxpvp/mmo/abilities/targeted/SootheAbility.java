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

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class SootheAbility extends BaseTargetedPlayerAbility{
	
	public final static String ABILITY_NAME = "Soothe";
	public final static String PERM_NODE = "soothe";
	
	private double healAmount;
	
	/**
	 * Gets the amount of health that will be given to the target
	 * 
	 * @return Double The amount to heal the target
	 */
	public double getHealAmount() {return healAmount;}
	
	/**
	 * Sets the amount of health to give to the target
	 * 
	 * @param healAmount The amount to heal the target
	 * @return SootheAbility This instance
	 */
	public SootheAbility setHealAmount(double healAmount) {this.healAmount = healAmount; return this;}
	
	/**
	 * Constructs a new Soothe Ability with the provided player as the user
	 * 
	 * @param player The ability's user
	 */
	public SootheAbility(Player player) {
		super(ABILITY_NAME, player, PlayerManager.getInstance().getPlayer(player).getTarget());
		
		this.healAmount = 8;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		LivingEntity t = getTarget();
		Location tLoc = t.getLocation(), loc = new Location(tLoc.getWorld(), tLoc.getX(), tLoc.getY()+1.75, tLoc.getZ());
		
		t.setHealth(t.getHealth() + getHealAmount());
		new ParticleRunner(ParticleType.heart, loc, false, 0, 1, (int) getHealAmount() / 2).start(0, 6);
		
		return false;
	}

}
