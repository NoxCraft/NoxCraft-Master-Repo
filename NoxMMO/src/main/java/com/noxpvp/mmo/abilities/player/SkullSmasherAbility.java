package com.noxpvp.mmo.abilities.player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.EffectsRunnable;

public class SkullSmasherAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Skull Smasher";
	public static final String PERM_NODE = "skull-smasher";
	
	private static Map<String, SkullSmasherAbility> smashers = new HashMap<String, SkullSmasherAbility>();
	
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
				if (event.getDamager().equals(SkullSmasherAbility.this.getPlayer()))
					SkullSmasherAbility.this.eventExecute(SkullSmasherAbility.this.getPlayer(), event.getDamage());
			}
		};
		
		this.range = range;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String name = getPlayer().getName();
		
		SkullSmasherAbility.smashers.put(name, this);
		PlayerClass pClass = PlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();
		
		int length = (20 * (pClass.getTotalLevel())) / 16;
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				if (SkullSmasherAbility.smashers.containsKey(name))
					SkullSmasherAbility.smashers.remove(name);
				
			}
		}, length);
		
		return true;
	}
	
	public void eventExecute(Player attacker, double damage){
		if (!smashers.containsKey(attacker.getName()))
			return;
		
		SkullSmasherAbility a = smashers.get(attacker.getName());
		
		for (Entity it : attacker.getNearbyEntities(a.range, a.range, a.range)){
			if (!(it instanceof Damageable)) continue;
			
			if (it == attacker) continue;
			
			Location itLoc = it.getLocation(),
					loc = new Location(itLoc.getWorld(), itLoc.getX(), itLoc.getY()+1.75, itLoc.getZ());
			
			((Damageable) it).damage(damage - (damage / 4), attacker);
			new EffectsRunnable(Arrays.asList("blockcrack_155_0"), false, loc, .1F, 20, 2, null).runTask(NoxMMO.getInstance());
		}
		
		return;
	}
	
}
