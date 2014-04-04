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
