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

package com.noxpvp.core.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.noxpvp.core.utils.TimeUtils;

public class CoolDown implements ConfigurationSerializable {
	private long expires;
	private final String name;
	private final boolean nanoTime;
	
	public CoolDown(String name, long expireStamp, boolean nanoTime)
	{
		this.name = name;
		this.expires = expireStamp;
		this.nanoTime = nanoTime;
	}
	
	public boolean expired() {
		long stamp = TimeUtils.getStamp(nanoTime);
		
		return expires <= stamp;
	}
	
	public final long getExpiryStamp() { return expires; }
	
	public final String getName() { return name; }
	
	public String getReadableTimeLeft() {
		if (isNanoTime())
			return TimeUtils.getReadableNanosTime(getTimeLeft());
		else
			return TimeUtils.getReadableMillisTime(getTimeLeft());
	}
	
	public final long getTimeLeft() {
		long stamp = TimeUtils.getStamp(nanoTime);
		return expires - stamp;
	}
	
	public final boolean isNanoTime() { return nanoTime; }

	public Map<String, Object> serialize() {
		Map<String, Object> obs = new HashMap<String, Object>();
		
		obs.put("name", getName());
		obs.put("expires", getExpiryStamp());
		obs.put("ns", isNanoTime());
		return obs;
	}
	
	public static CoolDown deserialize(Map<String, Object> obs)
	{
		if (!obs.containsKey("name")|| !obs.containsKey("expires")|| !obs.containsKey("ns"))
			throw new IllegalArgumentException("(name | expires | ns) should not be null!");
		return new CoolDown((String) obs.get("name"), (Long) obs.get("expires"), (Boolean) obs.get("ns")); 
	}
}
