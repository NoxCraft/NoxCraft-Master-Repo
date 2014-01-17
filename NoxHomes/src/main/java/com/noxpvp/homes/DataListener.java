package com.noxpvp.homes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.noxpvp.core.events.PlayerDataLoadEvent;
import com.noxpvp.core.events.PlayerDataSaveEvent;
import com.noxpvp.core.events.PlayerDataUnloadEvent;
import com.noxpvp.core.listeners.NoxListener;

public class DataListener extends NoxListener<NoxHomes> {

	public DataListener(NoxHomes plugin) {
		super(plugin);
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDataLoadEvent(PlayerDataLoadEvent event)
	{
		getPlugin().getHomeManager().loadHomes(event.getPlayer().getNoxPlayer().getName());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDataUnloadEvent(PlayerDataUnloadEvent event) 
	{
		getPlugin().getHomeManager().unload(event.getBukkitPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDataSaveEvent(PlayerDataSaveEvent event)
	{
		getPlugin().getHomeManager().getPlayer(event.getBukkitPlayer()).save();
	}
}
