package com.noxpvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.noxpvp.core.NoxCore;

public class OnLogoutSaveListener extends NoxListener<NoxCore> {

	public OnLogoutSaveListener(NoxCore plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onLogoutEvent(PlayerQuitEvent event) {
		getPlugin().getPlayerManager().unloadAndSavePlayer(event.getPlayer().getName());
	}
}
