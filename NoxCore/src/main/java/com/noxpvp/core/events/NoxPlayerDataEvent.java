package com.noxpvp.core.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

@Deprecated
public class NoxPlayerDataEvent extends Event {
	@Deprecated
	private static final HandlerList handlers = new HandlerList();
	private final NoxPlayerAdapter player;
	private boolean shouldHonorCore;

	@Deprecated
	public NoxPlayerDataEvent(NoxPlayerAdapter player, boolean honorCore) {
		this.player = player;
		this.shouldHonorCore = honorCore;
	}

	@Deprecated
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Deprecated
	public OfflinePlayer getBukkitPlayer() {
		return player.getNoxPlayer().getOfflinePlayer();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Deprecated
	public NoxPlayerAdapter getPlayer() {
		return player;
	}

	@Deprecated
	public boolean shouldHonorCore() {
		return shouldHonorCore;
	}
}
