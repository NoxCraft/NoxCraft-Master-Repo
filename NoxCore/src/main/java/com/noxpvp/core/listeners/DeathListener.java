package com.noxpvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.core.data.NoxPlayer;

public class DeathListener extends NoxListener {
	private PlayerManager pm;
	
	public DeathListener()
	{
		this(NoxCore.getInstance());
	}
	
	public DeathListener(NoxCore core)
	{
		super(core);
		this.pm = core.getPlayerManager();
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
