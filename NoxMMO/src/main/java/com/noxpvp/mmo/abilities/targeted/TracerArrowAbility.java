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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class TracerArrowAbility extends BaseTargetedPlayerAbility{
	
	static Map<String, TracerArrowAbility> abilityCue = new HashMap<String, TracerArrowAbility>();
	
	private final static String ABILITY_NAME = "Tracer Arrow";
	public final static String PERM_NODE = "tracer-arrow";
	
	/**
	 * Runs the event-side execution of this ability
	 * 
	 * @param p Player, normally arrow shooter from a projectile hit event
	 * @return boolean If the execution ran successfully
	 */
	public static boolean eventExecute(Player player, final Arrow arrow){
		MMOPlayer mmoP = PlayerManager.getInstance().getPlayer(player);
		String name = player.getName();
		
		if (abilityCue.containsKey(name))
			return false;
		
		final Arrow a = arrow;
		final LivingEntity target = mmoP.getTarget();
				
		Bukkit.getScheduler().runTaskTimer(NoxMMO.getInstance(), new BukkitRunnable() {
			
			private Vector arrowLoc = arrow.getLocation().toVector();
			private Vector los;
			
			public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}}
			
			public void run() {
				if (a == null || !a.isValid() || target == null){
					safeCancel(); return;}
				
				los = target.getLocation().toVector().subtract(arrowLoc);
				arrow.setVelocity(los);
			}
		}, 0, 1);
		return true;
	}

	public TracerArrowAbility(Player player) {
		super(ABILITY_NAME, player);
	}
	
	/**
	 * 
	 * @return boolean - If the ability executed successfully
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String pName = getPlayer().getName();
		
		TracerArrowAbility.abilityCue.put(pName, this);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				
				if (TracerArrowAbility.abilityCue.containsKey(pName))
					TracerArrowAbility.abilityCue.remove(pName);
				
			}
		}, 120);
		
		return true;
	}

}
