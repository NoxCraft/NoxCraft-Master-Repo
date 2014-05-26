package com.noxpvp.core.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.TimeUtils;

public class CoolDown implements ConfigurationSerializable {
	private final String name;
	private final boolean nanoTime;
	private final long expires;

	public CoolDown(String name, int seconds) {
		this(name, seconds * 1000, NoxCore.isUsingNanoTime());
	}
	
	public CoolDown(String name, int length, boolean nanoTime) {
		this(name, length + (nanoTime? System.nanoTime() : System.currentTimeMillis()), nanoTime);
	}
	
	public CoolDown(String name, long actualExpireTime, boolean nanoTime) {
		this.name = name;
		this.expires = actualExpireTime;
		this.nanoTime = nanoTime;
	}

	public boolean expired() {
		long stamp = TimeUtils.getStamp(nanoTime);

		return stamp >= expires;
	}

	public final String getName() {
		return name;
	}

	/**
	 * Gets the actual expire time in mili / nano seconds depending on {@link NoxCore#isUsingNanoTime()}
	 * 
	 * @return long Exact expire time
	 */
	public final long getExpiryStamp() {
		return expires;
	}

	/**
	 * Gets the time left on this cooldown in mili / nano seconds depending on {@link NoxCore#isUsingNanoTime()}
	 * 
	 * @return long Time left
	 */
	public final long getTimeLeft() {
		long stamp = TimeUtils.getStamp(nanoTime);
		return Math.max(expires - stamp, 0);
	}

	public String getReadableTimeLeft() {
		if (isNanoTime())
			return TimeUtils.getReadableSecTime(getTimeLeft() / 1000 / 1000);
		else
			return TimeUtils.getReadableMillisTime(getTimeLeft() / 1000);
	}

	/**
	 * If this cooldown is using nano time
	 * 
	 * @return {@link Boolean} nanoTime
	 */
	public final boolean isNanoTime() {
		return nanoTime;
	}

	public static CoolDown deserialize(Map<String, Object> obs) {
		if (!obs.containsKey("name") || !obs.containsKey("expires") || !obs.containsKey("ns"))
			throw new IllegalArgumentException("(name | expires | ns) should not be null!");
		
		return new CoolDown((String) obs.get("name"), (Long) obs.get("expires"), (Boolean) obs.get("ns"));
	}

	public Map<String, Object> serialize() {
		Map<String, Object> obs = new HashMap<String, Object>();

		obs.put("name", getName());
		obs.put("expires", getExpiryStamp());
		obs.put("ns", isNanoTime());
		
		return obs;
	}
}
