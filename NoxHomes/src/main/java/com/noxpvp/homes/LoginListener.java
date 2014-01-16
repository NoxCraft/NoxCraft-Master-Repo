package com.noxpvp.homes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

import com.noxpvp.core.listeners.NoxListener;

public class LoginListener extends NoxListener<NoxHomes> {

	public LoginListener(NoxHomes plugin) {
		super(plugin);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onLogin(PlayerLoginEvent e) {
		getPlugin().getHomeManager().loadHomes(e.getPlayer());
	}
	
}
