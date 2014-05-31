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

package com.noxpvp.core.internal;

/**
 * All implemented methods must be thread safe. 
 */
public interface SafeLocker {
	/**
	 * Check if the object is locked.
	 * @return true if locked and false if not.
	 */
	public boolean isLocked();
	
	/**
	 * Retrieves the object that locked this.
	 * @return null if isLocked is false or the object that locked this object.
	 */
	public LockerCaller getCaller();
	
	/**
	 * Attempts to lock this object.
	 * @param caller the object attempting to lock this object.
	 * @return true if successful false otherwise.
	 */
	public boolean tryLock(LockerCaller caller);
	
	/**
	 * Attempts to unlock this object.
	 * <p>
	 * By definition This must always not allow unlocking from other callers other that the one specified by getCaller().<br>
	 * <b>You must return false during such an action. Otherwise unexpected behavior will occur or possibly corrupt objects.</b>
	 * @param caller the object attempting to unlock this object.
	 * @return true if successful false otherwise.
	 */
	public boolean tryUnlock(LockerCaller caller);
}
