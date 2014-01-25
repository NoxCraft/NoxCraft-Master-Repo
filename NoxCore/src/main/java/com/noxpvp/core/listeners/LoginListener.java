package com.noxpvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.VaultAdapter;

public class LoginListener extends NoxListener<NoxCore> {
		public LoginListener()
		{
			this(NoxCore.getInstance());
		}
		
		public LoginListener(NoxCore core)
		{
			super(core);
		}
		
		@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
		public void onLogin(PlayerLoginEvent e)
		{
			Player p = e.getPlayer();
			
			VaultAdapter.GroupUtils.reloadGroupName(p);
			
			getPlugin().getPlayerManager().loadPlayer(e.getPlayer().getName());
		}
		
		@Override
		public void register() {
			super.register();
			CommonUtil.queueListenerLast(this, PlayerLoginEvent.class);
		}
}