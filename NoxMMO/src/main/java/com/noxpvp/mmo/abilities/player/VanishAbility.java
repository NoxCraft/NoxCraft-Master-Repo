package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.VanishPlayerRunnable;

public class VanishAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Cloak";
	public static final String PERM_NODE = "cloak";
	
	private double range;
	private int time;
	private int hideFreq;
	
	/**
	 * 
	 * @return double The currently set amount of distance to check for players to hide
	 */
	public double getRange() {return range;}

	/**
	 * 
	 * @param range The distance to check for players to hide
	 * @return VanishAbility This instance, used for chaining
	 */
	public VanishAbility setRange(double range) {this.range = range; return this;}

	/**
	 * 
	 * @return Integer The currently set amount of seconds to keep the player vanished
	 */
	public int getTime() {return time;}

	/**
	 * 
	 * @param time The amount of seconds to keep the player vanished
	 * @return VanishAbility This instance, used for chaining
	 */
	public VanishAbility setTime(int time) {this.time = time; return this;}

	/**
	 * 
	 * @return Integer The currently set amount of delay between checks for players to hidef
	 */
	public int getHideFreq() {return hideFreq;}

	/**
	 * 
	 * @param hideFreq The amount of time to wait between checks for players to hide
	 */
	public VanishAbility setHideFreq(int hideFreq) {this.hideFreq = hideFreq; return this;}

	/**
	 * 
	 * @param player The user of the ability instance
	 */
	public VanishAbility(Player player){
		super(ABILITY_NAME, player);
		
		this.range = 50;
		this.hideFreq = 20;
		this.time = 60;
	}

	/**
	 * 
	 * @return boolean If this ability executed successfully
	 */
	public boolean execute() {
		if(!mayExecute())
			return false;
		
		new VanishPlayerRunnable(getPlayer(), range, (time * (20 / hideFreq))).
				runTaskTimer(NoxMMO.getInstance(), 0, hideFreq);
		
		return true;
	}

}