package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class SeveringStrikesAbility extends BasePlayerAbility{
	
	private static final String ABILITY_NAME = "Severing Strikes";
	public static final String PERM_NODE = "severing-strikes";
	
	private static List<Player> strikers = new ArrayList<Player>();
	
	/**
	 * 
	 * @param player The user of the ability instance
	 */
	public SeveringStrikesAbility(Player player){
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
		
		SeveringStrikesAbility.strikers.add(p);
		
		int length = 0; //((tier * level) / 16) * 20ticks
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				if (SeveringStrikesAbility.strikers.contains(p))
					SeveringStrikesAbility.strikers.remove(p);
				
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
