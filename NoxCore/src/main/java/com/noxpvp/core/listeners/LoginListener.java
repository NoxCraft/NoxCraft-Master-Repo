package com.noxpvp.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.manager.PlayerManager;

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
		public void onLogin(PlayerJoinEvent e)
		{
			final Player p = e.getPlayer();
			
			VaultAdapter.GroupUtils.reloadGroupTag(p);
			PlayerManager.getInstance().loadPlayer(p.getName());
			CommonUtil.nextTick(new Runnable() {
				
				public void run() {
					PlayerManager.getInstance().getCoreBar(p.getName()).newShine(ChatColor.AQUA + "shining shining shining shining shining shining", 500, 0, true);
				}
			});
		}
		
		@Override
		public void register() {
			super.register();
			CommonUtil.queueListenerLast(this, PlayerJoinEvent.class);
		}
}