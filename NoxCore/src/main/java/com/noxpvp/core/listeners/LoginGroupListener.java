package com.noxpvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.VaultAdapter;

public class LoginGroupListener extends NoxListener<NoxCore> {
		public LoginGroupListener()
		{
			this(NoxCore.getInstance());
		}
		
		public LoginGroupListener(NoxCore core)
		{
			super(core);
		}
		
		@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
		public void onLogin(PlayerLoginEvent e)
		{
			Player p = e.getPlayer();
			
			VaultAdapter.GroupUtils.reloadGroupName(p);
		}
}