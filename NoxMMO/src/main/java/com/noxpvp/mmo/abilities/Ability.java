package com.noxpvp.mmo.abilities;

/**
 * <b>IF this is not a dynamic ability. <br/> With changing names. Please specify a static getName() method. </b>
 * @author Chris
 *
 */
public interface Ability {
	
	/**
	 * Retrieves the ability name.
	 * 
	 * @return Ability Name
	 */
	public String getName();
	
	/**
	 * Executes an ability
	 * @return true if successful and false if not.
	 */
	public boolean execute();
	
	/**
	 * Tells whether or not an ability should be allowed to be executed.
	 * @return true if allowed and false if not.
	 */
	public boolean mayExecute();
}
