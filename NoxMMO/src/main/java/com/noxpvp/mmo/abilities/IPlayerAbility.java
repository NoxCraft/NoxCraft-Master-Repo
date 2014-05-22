package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public interface IPlayerAbility extends IEntityAbility {
	
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
	public Entity getEntity();
	
	/**
	 * Retrieves the NoxPlayer core player implementation for this ability.
	 * <br/>
	 * This method should use getPlayer() within to retrieve this object if possible.
	 * 
	 * @return NoxPlayer object.
	 */
	public NoxPlayer getNoxPlayer();
	
	/**
	 * Retrieves the master listener to register and unregister handlers for this ability.
	 * 
	 * @return
	 */
	public MasterListener getMasterListener();
	
	/**
	 * <b>Helper method<b>
	 * 
	 * Used to register handlers with the master listener
	 * 
	 * @param handler
	 */
	public void registerHandler(BaseMMOEventHandler<? extends Event> handler);
	
	/**
	 * <b>Helper method<b>
	 * 
	 * Used to unregister handlers from the master listener
	 * 
	 * @param handler
	 */
	public void unregisterHandler(BaseMMOEventHandler<? extends Event> handler);
}
