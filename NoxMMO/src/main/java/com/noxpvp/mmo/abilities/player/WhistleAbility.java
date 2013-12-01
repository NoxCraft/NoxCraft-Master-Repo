package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class WhistleAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Whistle";
	public static final String PERM_NODE = "whistle";
	
	private int range = 15;
	
	/**
	 * 
	 * @return Integer The currently set range for this ability instance (Default is 15)
	 */
	public int getRange() {return range;}

	/**
	 * 
	 * @param range The range to check for nearby targets  (Default is 15)
	 * @return WhistleAbility This instance, used for chaining
	 */
	public WhistleAbility setRange(int range) {this.range = range; return this;}

	public WhistleAbility(Player player){super(ABILITY_NAME, player);}

	/**
	 * 
	 * @return boolean If the ability has executed successfully
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		for (Entity it : p.getNearbyEntities(range, range, range)){
			if (!(it instanceof Wolf)) continue;
			if (((Wolf)it).getOwner() != p) continue;
			
			Wolf n = (Wolf) it;
			
			if (n.isSitting()) n.setSitting(false);
			else if (!n.isSitting()) n.setSitting(true);
		}
		
		return true;
	}
	
	/**
	 * 
	 * @return boolean If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {return getPlayer() != null;}

}
