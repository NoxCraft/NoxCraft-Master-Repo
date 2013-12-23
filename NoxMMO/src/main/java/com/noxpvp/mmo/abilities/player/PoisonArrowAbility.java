package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class PoisonArrowAbility extends BasePlayerAbility{
	
	static Map<String, PoisonArrowAbility> abilityCue = new HashMap<String, PoisonArrowAbility>();
	
	private final static String ABILITY_NAME = "Poison Arrow";
	public final static String PERM_NODE = "poison-arrow";
	
	private int amplifier;
	private int duration;
	
	/**
	 * Runs the event-side execution of this ability
	 * 
	 * @param player The Player - normally arrow shooter from a damage event
	 * @param target The target - normally the living entity from a damage event
	 * @return boolean If the execution ran successfully
	 */
	public static boolean eventExecute(Player player, LivingEntity target){
		if (player == null || target == null)
			return false;
		
		String name = player.getName();
				
		if (abilityCue.containsKey(name))
			return false;
		
		PoisonArrowAbility a = abilityCue.get(name);
		
		target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, a.duration, a.amplifier));
		return true;
	}

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
	public PoisonArrowAbility setSize(int size) {this.amplifier = size; return this;}

	/**
	 * Gets the duration of the poison effect
	 * 
	 * @return Integer The duration
	 */
	public int getTime() {return duration;}

	/**
	 * Sets the duration in ticks of the poison amplifier
	 * 
	 * @param duration The duration
	 * @return NetArrowAbility This instance
	 */
	public PoisonArrowAbility setDuration(int duration) {this.duration = duration; return this;}

	public PoisonArrowAbility(Player player) {
		super(ABILITY_NAME, player);
		
		this.amplifier = 3;
		this.duration = 75;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String pName = getPlayer().getName();
		
		PoisonArrowAbility.abilityCue.put(getPlayer().getName(), this);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				
				if (PoisonArrowAbility.abilityCue.containsKey(pName))
					PoisonArrowAbility.abilityCue.remove(pName);
				
			}
		}, 100);
		
		return true;
	}

}
