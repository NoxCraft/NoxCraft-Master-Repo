package com.noxpvp.core.data;

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
public interface NoxPlayerAdapter {
	
	/**
	 * Retrieves the player object that this adaptor uses for data.
	 * @return NoxPlayer instance
	 */
	public NoxPlayer getNoxPlayer();
}
