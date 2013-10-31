package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;

import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class SuperBreakerAbility extends BasePlayerAbility {
	
	public static final String PERM_NODE = "super-breaker";
	private static final String ABILITY_NAME = "Super Breaker Ability";
	private static long maximumTicks = 6000;
	
	/**
	 * Retrieves the maximum number of ticks this ability can last for. 
	 * 
	 * This is the global limit.
	 * 
	 * @return the maximumTicks
	 */
	public static final long getMaximumTicks() {
		return maximumTicks;
	}

	/**
	 * @param maximumTicks the maximumTicks to set
	 */
	public static final void setMaximumTicks(long maximumTicks) {
		SuperBreakerAbility.maximumTicks = maximumTicks;
	}

	public SuperBreakerAbility(Player player)
	{
		super(ABILITY_NAME, player);
	}
	
	public SuperBreakerAbility(NoxPlayerAdapter adapt)
	{
		this(adapt.getNoxPlayer().getPlayer());
	}
	
	public boolean execute() {
		Player player = getPlayer();
		if (player == null)
			return false;
		
		
		// TODO Auto-generated method stub
		return false;
	}

	public boolean mayExecute() {
		// TODO Auto-generated method stub
		return false;
	}

}
