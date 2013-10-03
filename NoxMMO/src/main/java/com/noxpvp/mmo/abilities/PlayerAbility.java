package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Player;

import com.noxpvp.core.data.NoxPlayer;

public interface PlayerAbility extends EntityAbility {
	
	/**
	 * Retrieves the player that uses this ability.
	 * @return Player object that holds this ability
	 */
	public Player getPlayer();
	
	/**
	 * The entity is actually the return of getPlayer()
	 * 
	 * @see #getPlayer()
	 */
	public Player getEntity();
	
	/**
	 * Retrieves the NoxPlayer core player implementation for this ability.
	 * <br/>
	 * This method should use getPlayer() withen to retrieve this object if possible.
	 * 
	 * @return NoxPlayer object.
	 */
	public NoxPlayer getNoxPlayer();
}
