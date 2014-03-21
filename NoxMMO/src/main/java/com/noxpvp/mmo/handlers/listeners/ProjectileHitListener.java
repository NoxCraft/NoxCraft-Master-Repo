package com.noxpvp.mmo.handlers.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.GenericMMOListener;

public class ProjectileHitListener extends GenericMMOListener<ProjectileHitEvent> {
	public ProjectileHitListener(NoxMMO plugin) {
		super(plugin, ProjectileHitEvent.class);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onLowest(ProjectileHitEvent event) {
		onEventLowest(event);
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onLow(ProjectileHitEvent event) {
		onEventLow(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onNormal(ProjectileHitEvent event) {
		onEventNormal(event);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onHigh(ProjectileHitEvent event) {
		onEventHigh(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onHighest(ProjectileHitEvent event) {
		onEventHighest(event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onMonitor(ProjectileHitEvent event) {
		onEventMonitor(event);
	}
}
