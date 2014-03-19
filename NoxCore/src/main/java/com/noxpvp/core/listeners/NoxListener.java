package com.noxpvp.core.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.noxpvp.core.NoxPlugin;

public abstract class NoxListener <T extends NoxPlugin> implements Listener {
	private T plugin;
	private boolean isRegistered;
	
	public boolean isRegistered() {
		return isRegistered;
	}
	
	public NoxListener(T plugin)
	{
		this.plugin = plugin;
	}
	
	public T getPlugin() { return this.plugin; }
	
	public void register() { 
		if (isRegistered)
			return;
		plugin.register(this); 
		isRegistered = true;
	}
	
	public void unregister() {
		HandlerList.unregisterAll(this);
		isRegistered = false;
	}
}
