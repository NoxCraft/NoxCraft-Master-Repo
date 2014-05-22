package com.noxpvp.core.data;

public class ObjectLock {

	public final Object lock;
	public final boolean canUnlock;

	public ObjectLock(Object lock) {
		this(lock, true);
	}

	public ObjectLock(Object lock, boolean canUnlock) {
		this.lock = lock;
		this.canUnlock = canUnlock;
	}

}
