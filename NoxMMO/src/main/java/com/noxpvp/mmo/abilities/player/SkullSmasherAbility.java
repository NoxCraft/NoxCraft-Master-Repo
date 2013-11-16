package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class SkullSmasherAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Skull Smasher";
	public final static String PERM_NODE = "skull-smasher";
	
	private static List<Player> smashers = new ArrayList<Player>();
	
	/**
	 * 
	 * @param player The user of the ability instance
	 */
	public SkullSmasherAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * 
	 * @return boolean If this ability executed successfully
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final Player p = getPlayer();
		
		SkullSmasherAbility.smashers.add(p);
		
		int length = 0; //((tier * level) / 16) * 20ticks
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				if (SkullSmasherAbility.smashers.contains(p))
					SkullSmasherAbility.smashers.remove(p);
				
			}
		}, length);
		
		return true;
	}

	/**
	 * 
	 * @return boolean If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}

	
}
