package com.noxpvp.core.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.noxpvp.core.NoxPlugin;

public abstract class NoxListener <T extends NoxPlugin> implements Listener {
	private T plugin;
	
	public NoxListener(T plugin)
	{
		this.plugin = plugin;
	}
	
	public T getPlugin() { return this.plugin; }
	
	public void register() { plugin.register(this); }
	
	public void unregister() { HandlerList.unregisterAll(this); }
}
