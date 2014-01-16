package com.noxpvp.core.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

public class NoxPlayerDataEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private final NoxPlayerAdapter player;
 
	private boolean shouldHonorCore;
	
	public NoxPlayerDataEvent(NoxPlayerAdapter player, boolean honorCore)
	{
		this.player = player;
		this.shouldHonorCore = honorCore;
	}
	
	public NoxPlayerAdapter getPlayer() {
		return player;
	}
	
	public boolean shouldHonorCore() { return shouldHonorCore; }
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public OfflinePlayer getBukkitPlayer() {
		return player.getNoxPlayer().getOfflinePlayer();
	}
}
