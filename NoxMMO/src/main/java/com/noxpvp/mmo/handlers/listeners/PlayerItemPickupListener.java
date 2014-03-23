package com.noxpvp.mmo.handlers.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.GenericMMOListener;

public class PlayerItemPickupListener extends GenericMMOListener<PlayerPickupItemEvent> {
	
	public PlayerItemPickupListener(NoxMMO plugin) {
		super(plugin, PlayerPickupItemEvent.class);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onLowest(PlayerPickupItemEvent event) {
		onEventLowest(event);
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onLow(PlayerPickupItemEvent event) {
		onEventLow(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onNormal(PlayerPickupItemEvent event) {
		onEventNormal(event);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onHigh(PlayerPickupItemEvent event) {
		onEventHigh(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onHighest(PlayerPickupItemEvent event) {
		onEventHighest(event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onMonitor(PlayerPickupItemEvent event) {
		onEventMonitor(event);
	}
}
