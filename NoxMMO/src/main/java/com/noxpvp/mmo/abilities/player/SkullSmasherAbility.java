package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class SkullSmasherAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Skull Smasher";
	public static final String PERM_NODE = "skull-smasher";
	
	private static Map<String, SkullSmasherAbility> smashers = new HashMap<String, SkullSmasherAbility>();
	
	private double range;
	
	public static boolean eventExecute(Player attacker, double damage){
		if (!smashers.containsKey(attacker.getName()))
			return false;
		
		SkullSmasherAbility a = smashers.get(attacker.getName());
		
		for (Entity it : attacker.getNearbyEntities(a.range, a.range, a.range)){
			if (!(it instanceof Damageable)) continue;
			
			((Damageable) it).damage(damage - (damage / 4), attacker);
		}
		
		return true;
	}

	/**
	 * gets the currently set range for this ability
	 * 
	 * @return double The Range
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
		
		this.range = range;
	}
	
	/**
	 * 
	 * @return boolean If this ability executed successfully
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String name = getPlayer().getName();
		
		SkullSmasherAbility.smashers.put(name, this);
		PlayerClass pClass = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(getPlayer()).getMainPlayerClass();
		
		int length = 20 * ((pClass.getLevel() * pClass.getTierLevel()) / 16);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				if (SkullSmasherAbility.smashers.containsKey(name))
					SkullSmasherAbility.smashers.remove(name);
				
			}
		}, length);
		
		return true;
	}
	
}
