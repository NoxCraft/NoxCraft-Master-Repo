package com.noxpvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.manager.PlayerManager;

public class OnLogoutSaveListener extends NoxListener<NoxCore> {

	public OnLogoutSaveListener(NoxCore plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onLogoutEvent(PlayerQuitEvent event) {
		PlayerManager.getInstance().unloadAndSavePlayer(event.getPlayer().getName());
	}
}
