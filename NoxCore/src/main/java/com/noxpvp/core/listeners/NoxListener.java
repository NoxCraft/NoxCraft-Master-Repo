package com.noxpvp.core.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.noxpvp.core.NoxPlugin;

public abstract class NoxListener implements Listener {
	private NoxPlugin plugin;
	
	public NoxListener(NoxPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	public void register() { plugin.register(this); }
	
	public void unregister() { HandlerList.unregisterAll(this); }
}
