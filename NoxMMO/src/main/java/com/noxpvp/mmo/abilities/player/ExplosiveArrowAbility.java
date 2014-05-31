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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class ExplosiveArrowAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Explosive Arrow";
	public final static String PERM_NODE = "explosive-arrow";
	
	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;
	private BaseMMOEventHandler<ProjectileLaunchEvent> launchHandler;
	
	List<Arrow> arrows;
	private boolean isActive, isFiring, isSingleShotMode;
	private float power;
	
	public ExplosiveArrowAbility setFiring(boolean firing) { 
		boolean changed = this.isFiring != firing;
		this.isFiring = firing;
		
		if (changed)
			if (firing)
				registerHandler(launchHandler);
			else
				unRegisterHandler(hitHandler);
		
		return this; 
	}
	
	public ExplosiveArrowAbility setActive(boolean active) {
		boolean changed = this.isActive != active;
		this.isActive = active;
		
		if (changed)
			if (active)
				registerHandler(hitHandler);
			else
				unRegisterHandler(hitHandler);
		
		return this; 
	}
	
	public ExplosiveArrowAbility setSingleShotMode(boolean singleMode) { this.isSingleShotMode = singleMode; return this; }
	
	public boolean isFiring() { return this.isFiring; }
	public boolean isActive() { return this.isActive; }
	public boolean isSingleShotMode() { return this.isSingleShotMode;}
	
	
	/**
	 * Gets the current power of the explosion
	 * 
	 * @return Float The power
	 */
	public float getPower() {return power;}

	/**
	 * Sets the power of the explosion
	 * 
	 * @param power The power
	 * @return NetArrowAbility This instance
	 */
	public ExplosiveArrowAbility setPower(float power) {this.power = power; return this;}

	public ExplosiveArrowAbility(Player player) {
		super(ABILITY_NAME, player);
		
		hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public void execute(ProjectileHitEvent event) {
				if (event.getEntityType() != EntityType.ARROW)
					return;
				
				Arrow a = (Arrow) event.getEntity();
				
				Location loc = a.getLocation();
				if (!arrows.contains(a))
					return;
				
				a.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, false, false);
				arrows.remove(a);
				a.remove();
				
				if (arrows.isEmpty())
					setActive(false);
			}

			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}

			public String getEventName() {
				return "ProjectileHitEvent";
			}
		};
		
		launchHandler = new BaseMMOEventHandler<ProjectileLaunchEvent>(new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileLaunchEvent").toString(), EventPriority.MONITOR, 1) {
			
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<ProjectileLaunchEvent> getEventType() {
				return ProjectileLaunchEvent.class;
			}
			
			public String getEventName() {
				return "ProjectileLaunchEvent";
			}
			
			public void execute(ProjectileLaunchEvent event) {
				Arrow a = (event.getEntity() instanceof Arrow? (Arrow)event.getEntity() : null);
				
				if (a == null)
					return;
				
				if (a.getShooter().equals(getPlayer()) && isFiring())
				{
					arrows.add(a);
					if (isSingleShotMode())
						setFiring(false);
				}
				
				if (!arrows.isEmpty())
					setActive(true);
				
			}
		};
		
		
		this.arrows = new ArrayList<Arrow>();
		this.isActive = false;
		this.isFiring = false;
		this.isSingleShotMode = true;
		this.power = 4f;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		if (!isActive() && !isFiring())
		{
			setFiring(true);
			MessageUtil.sendLocale(NoxMMO.getInstance(), getPlayer(), "ability.activated", ABILITY_NAME);
			return true;
		}
		else
		{
			MessageUtil.sendLocale(NoxMMO.getInstance(), getPlayer(), "ability.already-active", ABILITY_NAME);
			return false;
		}
	}

}
