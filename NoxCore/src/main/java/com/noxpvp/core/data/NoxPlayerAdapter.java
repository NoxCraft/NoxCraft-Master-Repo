package com.noxpvp.core.data;

import org.bukkit.entity.Player;

import com.noxpvp.core.Persistant;

/**
 * Using this interface you are required to implement a constructor with the
 * argument NoxPlayer as part of the implementation. <br />
 * 
 * After doing so. It will allow the core to automatically give you objects of
 * desired choice.<br />
 * 
 * Failure to do so if you register your object as a player object. Will result
 * in it not automatically being converted when supplied.
 * 
 * @author Chris
 */
public interface NoxPlayerAdapter extends Persistant {
	
	/**
	 * Retrieves the player object that this adaptor uses for data.
	 * @return NoxPlayer instance
	 */
	public NoxPlayer getNoxPlayer();
	
	/**
	 * Retrieves the player represented by this object.
	 * @return Player
	 */
	public Player getPlayer();
	
	public String getPlayerName();
}
