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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.BlockTimerRunnable;

public class NetArrowAbility extends BasePlayerAbility
{
	
	private List<Arrow> arrows;
	
	public final static String ABILITY_NAME = "Net Arrow";
	public final static String PERM_NODE = "net-arrow";
	
	/**
	 * Runs the event-side execution of this ability
	 * 
	 * @param p Player, normally arrow shooter from a projectile hit event
	 * @param loc The location to make the net, normally the location of an arrow
	 * @return boolean If the execution ran successfully
	 */
	private void eventExecute(Arrow a, int time)
	{
		
		Location loc = a.getLocation();
		
		List<Block> net = new ArrayList<Block>();
		
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		int size = getSize();
		
		for (int by = y+(size/2); by > y-(size / 2); by--)
			for (int bx = x+(size/2); bx > x-(size/2); bx--)
				for (int bz = z+(size/2); bz > z-(size/2); bz--) 
				{
					Block b = a.getWorld().getBlockAt(bx, by, bz);
					
					if (!isNetable(b.getType())) continue;
					
					b.setType(Material.WEB);
					net.add(b);
					
				}
		
		BlockTimerRunnable netRemover = new BlockTimerRunnable(net, Material.AIR, Material.WEB);
		netRemover.runTaskLater(NoxMMO.getInstance(), time);
		
		
		arrows.remove(a);
		
		if (arrows.isEmpty())
			setActive(false);
		
		return;
	}
	
	private static boolean isNetable(Material type)
	{
		switch(type){
			case AIR:
			case LONG_GRASS:
			case CROPS:
			case VINE:
			case WATER_LILY:
				return true;
			default:
				return false;
		}
	}
	
	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;
	private BaseMMOEventHandler<ProjectileLaunchEvent> launchHandler;
	
	private int size;
	private int time;
	private boolean isFiring, isActive, isSingleShotMode;
	
	public NetArrowAbility setFiring(boolean firing){
		boolean changed = this.isFiring != firing;
		this.isFiring = firing;
		
		MasterListener m = NoxMMO.getInstance().getMasterListener();
		
		if (changed)
			if (firing)
				m.registerHandler(launchHandler);
			else
				m.unregisterHandler(launchHandler);
		
		return this;
	}
	
	public NetArrowAbility setActive(boolean active){
		boolean changed = this.isActive != active;
		this.isActive = active;
		
		MasterListener m = NoxMMO.getInstance().getMasterListener();
		
		if (changed)
			if (active)
				m.registerHandler(hitHandler);
			else
				m.unregisterHandler(hitHandler);
		
		return this;
	}
	
	public NetArrowAbility setSingleShotMode(boolean single) {
		this.isSingleShotMode = single;
		return this;
	}
	
	public boolean isSingleShotMode() { return this.isSingleShotMode; }
	public boolean isActive() { return this.isActive; }
	public boolean isFiring() { return this.isFiring; }
	
	/**
	 * Get the currently set size of the net
	 * 
	 * @return Integer The net size
	 */
	public int getSize() {return size;}

	/**
	 * Sets the size of the net
	 * 
	 * @param size The width of the net itself. This should be a odd number
	 * @return NetArrowAbility This instance
	 */
	public NetArrowAbility setSize(int size) {this.size = size; return this;}

	/**
	 * Gets the time the net will stay until removed
	 * 
	 * @return Integer Time in ticks
	 */
	public int getTime() {return time;}

	/**
	 * Sets the time in ticks that the net will stay
	 * 
	 * @param time The time in ticks
	 * @return NetArrowAbility This instance
	 */
	public NetArrowAbility setTime(int time) {this.time = time; return this;}

	public NetArrowAbility(Player player)
	{
		super(ABILITY_NAME, player);
		
		hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public void execute(ProjectileHitEvent event) {
				Arrow a = (event.getEntity() instanceof Arrow)? (Arrow) event.getEntity() : null ;
				
				if (a == null)
					return;
				
				if (arrows.contains(a))
					eventExecute(a, time);
			}

			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}

			public String getEventName() {
				return "ProjectileHitEvent";
			}
		};

		this.arrows = new ArrayList<Arrow>();
		
		this.size = 3;
		this.time = 100;
		
		this.isActive = false;
		this.isFiring = false;
		this.isSingleShotMode = true;
	}
	
	public boolean execute(){
		if (!mayExecute())
			return false;
		
		if (!isActive() && !isFiring()) {
			setFiring(true);
			MessageUtil.sendLocale(NoxMMO.getInstance(), getPlayer(), "ability.arrow.net.use");
		} else
			MessageUtil.sendLocale(NoxMMO.getInstance(), getPlayer(), "ability.already-active", ABILITY_NAME);
		
		return true;
	}

}
