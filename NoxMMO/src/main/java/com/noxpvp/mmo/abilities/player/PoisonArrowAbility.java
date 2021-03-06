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

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class PoisonArrowAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Poison Arrow";
	public final static String PERM_NODE = "poison-arrow";
	
	private BaseMMOEventHandler<EntityDamageByEntityEvent> hitHandler;
	private BaseMMOEventHandler<ProjectileLaunchEvent> launchHandler;

	private int amplifier;
	private int duration;
	
	List<Arrow> arrows = new ArrayList<Arrow>();
	private boolean isActive = false, isFiring = false, isSingleShotMode = true;
	
	public PoisonArrowAbility setFiring(boolean firing) { 
		boolean changed = this.isFiring != firing;
		this.isFiring = firing;
		
		MasterListener m = NoxMMO.getInstance().getMasterListener();
		
		if (changed)
			if (firing)
				m.registerHandler(launchHandler);
			else
				m.unregisterHandler(hitHandler);
		
		return this; 
	}
	
	public PoisonArrowAbility setActive(boolean active) {
		boolean changed = this.isActive != active;
		this.isActive = active;
		
		if (changed)
			if (active)
				registerHandler(hitHandler);
			else
				unRegisterHandler(hitHandler);
		
		return this; 
	}
	
	public PoisonArrowAbility setSingleShotMode(boolean singleMode) { this.isSingleShotMode = singleMode; return this; }
	
	public boolean isFiring() { return this.isFiring; }
	public boolean isActive() { return this.isActive; }
	public boolean isSingleShotMode() { return this.isSingleShotMode;}
	
	/**
	 * Get the currently set poison effect amplifier
	 * 
	 * @return Integer The poison amplifier
	 */
	public int getAmplifier() {return amplifier;}

	/**
	 * Sets the poison effect amplifier
	 * 
	 * @param size The poison effect amplifier
	 * @return NetArrowAbility This instance
	 */
	public PoisonArrowAbility setAmplifier(int size) {this.amplifier = size; return this;}

	/**
	 * Gets the duration of the poison effect
	 * 
	 * @return Integer The duration
	 */
	public int getDuration() {return duration;}

	/**
	 * Sets the duration in ticks of the poison amplifier
	 * 
	 * @param duration The duration
	 * @return NetArrowAbility This instance
	 */
	public PoisonArrowAbility setDuration(int duration) {this.duration = duration; return this;}

	public PoisonArrowAbility(Player player) {
		super(ABILITY_NAME, player);
		
		hitHandler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public void execute(EntityDamageByEntityEvent event) {
				if (event.getDamager().getType() != EntityType.ARROW)
					return;
				if (!mayExecute())
					return;
				
				LivingEntity e = (LivingEntity) ((event.getEntity() instanceof LivingEntity)? event.getEntity(): null);
				if (e == null)
					return;
				
				
				Arrow a = (Arrow) event.getDamager();		
				
				if (a.getShooter().getType() != EntityType.PLAYER)
					return;
				
				if (a.getShooter().equals(getPlayer()))
				
				if (PoisonArrowAbility.this.arrows.contains(a))
					e.addPotionEffect(new PotionEffect(PotionEffectType.POISON, getDuration(), getAmplifier()));
				
				arrows.remove(a);
				
				if (arrows.isEmpty())
					setActive(false);
				
				else return;
			}

			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}

			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}
		};
		
		launchHandler = new BaseMMOEventHandler<ProjectileLaunchEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileLaunchEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			
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
		
		this.amplifier = 3;
		this.duration = 75;
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
