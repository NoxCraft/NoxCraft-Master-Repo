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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.data.Vector3D;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class TargetAbility extends BasePlayerAbility implements PassiveAbility<PlayerInteractEvent>{
	
	public static final String PERM_NODE = "target";
	public static final String ABILITY_NAME = "Target";
	
	private double range;
	private Reference<LivingEntity> target_ref;
	
	/**
	 * 
	 * 
	 * @return double - The currently set range for target distance
	 */
	public double getRange() {return range;}
	
	/**
	 * 
	 * @param range - The double range to look for targets
	 * @return TargetAbility - This instance, used for chaining
	 */
	public TargetAbility setRange(double range) {this.range = range; return this;}
	
	/**
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public TargetAbility(Player player){
		super(ABILITY_NAME, player);
		
		this.range = 25;
	}
	
	/**
	 * 
	 * @return Boolean - PassiveAbililty, return true
	 */
	public boolean execute() { return true; }
	
	public boolean execute(PlayerInteractEvent event){
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		for (Entity it : p.getNearbyEntities(range, range, range)){
			
			if (!(it instanceof LivingEntity) || it.equals(p)) continue;
			if ((it instanceof Player) && !(p).canSee((Player) it)) continue;
			
			Location observerPos = p.getEyeLocation();
			Vector3D observerDir = new Vector3D(observerPos.getDirection());
			
			Vector3D observerStart = new Vector3D(observerPos);
			Vector3D observerEnd = observerStart.add(observerDir.multiply(range));
			
			Vector3D targetPos = new Vector3D(it.getLocation());
			Vector3D minimum = targetPos.add(-0.6, 0, -0.6); 
			Vector3D maximum = targetPos.add(0.6, 2.0, 0.6); 
			
			if (hasIntersection(observerStart, observerEnd, minimum, maximum)) {
				this.target_ref = new SoftReference<LivingEntity>((LivingEntity) it);
				
				PlayerManager pm = PlayerManager.getInstance();
				MMOPlayer mmoPlayer = pm.getPlayer(p), mmoIt = it instanceof Player? pm.getPlayer((Player) it) : null;
				com.noxpvp.core.manager.PlayerManager cpm = com.noxpvp.core.manager.PlayerManager.getInstance();
				
				if (mmoPlayer == null) continue;
				
				mmoPlayer.setTarget(target_ref.get());
				
				String name;
				CoreBar bar =  cpm.getPlayer(p.getName()).getCoreBar();
				
				if (mmoIt != null){
					PlayerClass c = mmoPlayer.getPrimaryClass();
					
					if (c != null) { 
						name = mmoIt.getFullName() + bar.color + bar.separater + ChatColor.RESET + c.getDisplayName();
					} else name = mmoIt.getFullName();
				} else {
					if (it instanceof Player) name = ((Player)it).getName();
					else name = it.getType().name();
				} 
				
				bar.newLivingTracker(target_ref.get(), name, false);
				return true;
			} else {
				continue;
			}
		}
		
		return true;
	}
	
	private boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
		final double epsilon = 0.0001f;
 
		Vector3D d = p2.subtract(p1).multiply(0.5);
		Vector3D e = max.subtract(min).multiply(0.5);
		Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
		Vector3D ad = d.abs();
 
		if (Math.abs(c.x) > e.x + ad.x)
			return false;
		if (Math.abs(c.y) > e.y + ad.y)
			return false;
		if (Math.abs(c.z) > e.z + ad.z)
			return false;
 
		if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
			return false;
		if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
			return false;
		if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
			return false;

		return true;
	}

}
