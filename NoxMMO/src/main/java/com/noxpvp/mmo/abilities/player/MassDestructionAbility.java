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

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.ExpandingDamageRunnable;
import com.noxpvp.mmo.runnables.SetVelocityRunnable;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;

/**
 * @author NoxPVP
 *
 */
public class MassDestructionAbility extends BasePlayerAbility{
	
	public static final String PERM_NODE = "mass-destruction";
	public static final String ABILITY_NAME = "Mass Destruction";
	
	private BaseMMOEventHandler<EntityDamageEvent> handler;
	private double damage = 6;
	private double hVelo = 4;
	private int range = 6;
	
	/**
	 * 
	 * @return Double The current damage set for this ability
	 */
	public double getDamage() {return damage;}

	/**
	 * 
	 * @param damage
	 * @return MassDestructionAbility This instance
	 */
	public MassDestructionAbility setDamage(double damage) {this.damage = damage; return this;}

	/**
	 * 
	 * @param velo Double velocity value for player upwards/downwards effect
	 * @return MassDestructionAbility This instance, used for chaining
	 */
	public MassDestructionAbility sethVelo(double velo) {this.hVelo = velo; return this;}
	
	/**
	 * 
	 * @return Double The current set velocity used for the player upwards/downwards effect
	 */
	public double gethVelo() {return this.hVelo;}
	
	/**
	 * 
	 * @return Integer The current range
	 */
	public int getRange() {return range;}

	/**
	 * 
	 * @param range
	 * @return MassDestructionAbility This instance
	 */
	public MassDestructionAbility setRange(int range) {this.range = range; return this;}

	/**
	 * 
	 * @param p The Player type user for this instance
	 */
	public MassDestructionAbility(Player p){
		super(ABILITY_NAME, p);
		
		handler = new BaseMMOEventHandler<EntityDamageEvent>(
				new StringBuilder().append(p.getName()).append(ABILITY_NAME).append("EntityDamageEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<EntityDamageEvent> getEventType() {
				return EntityDamageEvent.class;
			}
			
			public String getEventName() {
				return "EntityDamageEvent";
			}
			
			public void execute(EntityDamageEvent event) {
				if (event.getCause() != DamageCause.FALL || event.getEntityType() != EntityType.PLAYER)
					return;
				
				Player p = (Player) event.getEntity();
				
				if (p.equals(MassDestructionAbility.this.getPlayer()))
					MassDestructionAbility.this.eventExecute(MassDestructionAbility.this);
			}
		};
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		NoxMMO instance = NoxMMO.getInstance();
		
		final Player p = getPlayer();
		
		Vector up = p.getLocation().getDirection();
		up.setY(gethVelo());
		Vector down = p.getLocation().getDirection();
		down.setY(-gethVelo());
		
		SetVelocityRunnable shootUp = new SetVelocityRunnable(getEntity(), up);
		SetVelocityRunnable shootDown = new SetVelocityRunnable(getEntity(), down);
		
		shootUp.runTask(instance);
		shootDown.runTaskLater(instance, 30);
	
		return true;
	}
	
	public void eventExecute(MassDestructionAbility ab) {
		
		Player p = ab.getPlayer();
		Location pLoc = p.getLocation();
		
		int range = ab.getRange();
		NoxMMO mmo = NoxMMO.getInstance();
		
		new ParticleRunner(ParticleType.explode, pLoc, false, 0, 2, 1).runTask(mmo);
		new ShockWaveAnimation(p, pLoc, 2, range, 0.3).runTask(mmo);
		new ExpandingDamageRunnable(p, p.getLocation(), ab.getDamage(), range, 2).runTask(mmo);
	}

}
