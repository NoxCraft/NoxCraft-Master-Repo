package com.noxpvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.events.uuid.NoxUUIDFoundEvent;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.UUIDUtil;
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
		updateLoginMessage();
	}
	
	public void updateLoginMessage() {
		loginMessage = MessageUtil.parseColor(getPlugin().getCoreConfig().get("motd.login", String.class, "&6Welcome to &cNoxImperialis!"));
	}
	
	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onLogin(PlayerJoinEvent e)
	{
		final Player p = e.getPlayer();
		UUIDUtil.getInstance().ensurePlayerUUIDsByName(UUIDUtil.toList(p.getName()));
		
		VaultAdapter.GroupUtils.reloadGroupTag(p);
		NoxPlayer nPlayer = PlayerManager.getInstance().getPlayer(p);
		
		nPlayer.getCoreBar().newShine(loginMessage, 0, 500, true);
		PlayerManager.getInstance().loadPlayer(nPlayer);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onUUIDFound(final NoxUUIDFoundEvent event) {
		if (event.isAsynchronous()) //safety check.
		{
			CommonUtil.nextTick(new Runnable() {
				public void run() {
					PlayerManager pm = PlayerManager.getInstance();
					if (pm.isLoaded(event.getUsername()))
						pm.loadPlayer(event.getUsername());
				}
			});
		} else {
			PlayerManager pm = PlayerManager.getInstance();
			if (pm.isLoaded(event.getUsername()))
				pm.loadPlayer(event.getUsername());
		}
	}
	
	@Override
	public void register() {
		super.register();
		CommonUtil.queueListenerLast(this, PlayerJoinEvent.class);
	}
}