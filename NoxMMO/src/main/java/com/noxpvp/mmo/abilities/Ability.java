/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

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
	 * Retrieves the display name locale of this ability.
	 * @return Colored String Value
	 */
	public String getDisplayName();
	
	/**
	 * Tells whether or not an ability should be allowed to be executed.
	 * @return true if allowed and false if not.
	 */
	public boolean mayExecute();
}
