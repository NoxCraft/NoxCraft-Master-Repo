package com.noxpvp.mmo.handlers.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.GenericMMOListener;

public class ItemPickupListener extends GenericMMOListener<InventoryPickupItemEvent> {
	public ItemPickupListener(NoxMMO plugin) {
		super(plugin, InventoryPickupItemEvent.class);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onLowest(InventoryPickupItemEvent event) {
		onEventLowest(event);
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onLow(InventoryPickupItemEvent event) {
		onEventLow(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onNormal(InventoryPickupItemEvent event) {
		onEventNormal(event);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onHigh(InventoryPickupItemEvent event) {
		onEventHigh(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onHighest(InventoryPickupItemEvent event) {
		onEventHighest(event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onMonitor(InventoryPickupItemEvent event) {
		onEventMonitor(event);
	}
}
