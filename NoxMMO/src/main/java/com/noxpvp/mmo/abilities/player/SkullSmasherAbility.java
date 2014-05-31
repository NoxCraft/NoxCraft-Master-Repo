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

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.core.utils.StaticEffects;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.UnregisterMMOHandlerRunnable;

public class SkullSmasherAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Skull Smasher";
	public static final String PERM_NODE = "skull-smasher";
	
	private BaseMMOEventHandler<EntityDamageByEntityEvent> handler;
	private double range;

	/**
	 * gets the currently set range for this ability
	 * 
	 * @return double The Ranged
	 */
	public double getRange() {return range;}

	/**
	 * Sets the range for this ability
	 * 
	 * @param range The range
	 * @return SkullSmasherAbility This instance
	 */
	public SkullSmasherAbility setRange(double range) {this.range = range; return this;}

	/**
	 * 
	 * @param player The user of the ability instance
	 */
	public SkullSmasherAbility(Player player, double range){
		super(ABILITY_NAME, player);
		
		this.handler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}
			
			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}
			
			public void execute(EntityDamageByEntityEvent event) {
				if (!event.getDamager().equals(SkullSmasherAbility.this.getPlayer()))
					return;
				
				double damage = event.getDamage();
				double range = SkullSmasherAbility.this.range;
				Player attacker = (Player) event.getDamager();
				
				for (Entity it : getPlayer().getNearbyEntities(range, range, range)){
					if (!(it instanceof Damageable) || it.equals(attacker)) continue;
					
					((Damageable) it).damage(damage - (damage / 4), attacker);
					StaticEffects.SkullBreak((LivingEntity) it);
				}
			}
		};
		
		this.range = range;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		PlayerClass pClass = PlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();
		
		int length = (20 * (pClass.getTotalLevel())) / 16;
		
		registerHandler(handler);
		new UnregisterMMOHandlerRunnable(handler).runTaskLater(NoxMMO.getInstance(), length);
		
		return true;
	}
	
}
