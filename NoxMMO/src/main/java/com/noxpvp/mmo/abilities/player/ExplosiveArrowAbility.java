package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class ExplosiveArrowAbility extends BasePlayerAbility{
	
	static Map<String, ExplosiveArrowAbility> abilityCue = new HashMap<String, ExplosiveArrowAbility>();
	
	private final static String ABILITY_NAME = "Explosive Arrow";
	public final static String PERM_NODE = "explosive-arrow";
	
	/**
	 * Runs the event-side execution of this ability
	 * 
	 * @param player The Player - normally arrow shooter from a projectile hit event
	 * @param loc The location - normally the hit block from a projectile hit event
	 * @return boolean If the execution ran successfully
	 */
	public static boolean eventExecute(Player player, Location loc){
		if (player == null || loc == null)
			return false;
		
		String name = player.getName();
				
		if (abilityCue.containsKey(name))
			return false;
		
		player.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), abilityCue.get(name).power, false, false);
		return true;
	}
	
	private float power;

	/**
	 * Gets the current power of the explosion
	 * 
	 * @return Integer The power
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
		
		this.power = 4;
	}
	
	/**
	 * 
	 * @return boolean - If the ability executed successfully
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String pName = getPlayer().getName();
		
		ExplosiveArrowAbility.abilityCue.put(getPlayer().getName(), this);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				
				if (ExplosiveArrowAbility.abilityCue.containsKey(pName))
					ExplosiveArrowAbility.abilityCue.remove(pName);
				
			}
		}, 100);
		
		return true;
	}
	
	/**
	 * 
	 * @return boolean - If the execute() method will normally be able to execute
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}

}
