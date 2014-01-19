package com.noxpvp.core.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

public class NoxPlayerDataEvent extends Event {
	private final NoxPlayerAdapter player;
	
	private boolean shouldHonorCore;
 
	public NoxPlayerDataEvent(NoxPlayerAdapter player, boolean honorCore)
	{
		this.player = player;
		this.shouldHonorCore = honorCore;
	}
	
	public OfflinePlayer getBukkitPlayer() {
		return player.getNoxPlayer().getOfflinePlayer();
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public NoxPlayerAdapter getPlayer() {
		return player;
	}
	
	public boolean shouldHonorCore() { return shouldHonorCore; }
	
	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
