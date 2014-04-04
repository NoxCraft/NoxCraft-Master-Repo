package com.noxpvp.core.internal;

public interface LockerCaller {
	/**
	 * Complain about a deadlock to this locker.
	 * @param lock being complained about.
	 */
	public void complain(SafeLocker lock);

	/**
	 * Complains about a deadlock to this locker.
	 * @param lock being complained about.
	 * @param complainer the complainer
	 */
	public void complain(SafeLocker lock, LockerCaller complainer);
}
