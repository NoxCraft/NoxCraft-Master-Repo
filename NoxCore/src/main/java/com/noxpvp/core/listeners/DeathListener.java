package com.noxpvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.core.data.NoxPlayer;

public class DeathListener implements Listener {
	private PlayerManager pm;
	
	public DeathListener()
	{
		this(NoxCore.getInstance().getPlayerManager());
	}
	
	public DeathListener(PlayerManager manager)
	{
		this.pm = manager;
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent e)
	{
		Player p = null;
		NoxPlayer player = pm.getPlayer(p = e.getEntity());
		if (player == null)
			return;
		
		player.setLastDeathTS();
		player.setLastDeathLocation(p.getLocation());
	}
}
