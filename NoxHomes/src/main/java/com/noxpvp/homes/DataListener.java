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
		PlayerManager.getInstance().getPlayer(event.getPlayer().getNoxPlayer().getName()).load(event.isOverwriting());;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDataUnloadEvent(PlayerDataUnloadEvent event) 
	{
		PlayerManager.getInstance().unloadPlayer(event.getBukkitPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDataSaveEvent(PlayerDataSaveEvent event)
	{
		PlayerManager.getInstance().getPlayer(event.getBukkitPlayer()).save(false);;
	}
}
