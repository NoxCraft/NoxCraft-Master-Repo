package com.noxpvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.gui.MessageUtil;

public class LoginListener extends NoxListener<NoxCore> {
		
	private String loginMessage;
		
	public LoginListener()
	{
		this(NoxCore.getInstance());
	}
	
	public LoginListener(NoxCore core)
	{
		super(core);
		
		loginMessage = MessageUtil.parseColor(core.getCoreConfig().get("motd.login", String.class, "&6Welcome to &cNoxImperialis!"));
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void onLogin(PlayerJoinEvent e)
	{
		final Player p = e.getPlayer();
		
		VaultAdapter.GroupUtils.reloadGroupTag(p);
		NoxPlayer nPlayer = PlayerManager.getInstance().getPlayer(p);
		
		nPlayer.getCoreBar().newShine(loginMessage, 0, 500, true);

	}
	
	@Override
	public void register() {
		super.register();
		CommonUtil.queueListenerLast(this, PlayerJoinEvent.class);
	}
}