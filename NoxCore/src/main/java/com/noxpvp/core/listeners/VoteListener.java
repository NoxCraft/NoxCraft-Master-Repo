package com.noxpvp.core.listeners;

import java.io.IOException;
import java.util.logging.FileHandler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.core.data.NoxPlayer;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener implements Listener {
	private final PlayerManager manager;
	private static ModuleLogger log;
	private static FileHandler handle;
	
	public VoteListener()
	{
		manager = NoxCore.getInstance().getPlayerManager();
		destroy();
		init();
	}
	
	private void init()
	{
		try {
			handle = new FileHandler(NoxCore.getInstance().getDataFile("votelogs.log").getPath(), true);
			log = NoxCore.getInstance().getModuleLogger("Vote","Log");
			log.addHandler(handle);
		} catch (SecurityException e) {
			if (handle != null)
				handle.close();
			e.printStackTrace();
		} catch (IOException e) {
			if (handle != null)
				handle.close();
			e.printStackTrace();
		}
	}
	
	public void destroy()
	{
		if (handle != null)
		{
			log.removeHandler(handle);
			handle.close();
		}
		
		HandlerList.unregisterAll(this);
			
		handle = null;
	}
	
	@EventHandler(priority= EventPriority.MONITOR)
	public void onVote(VotifierEvent event)
	{
		Vote vote = event.getVote();
		boolean usePlayer = false;
		
		if (vote.getUsername().length() > 4)
			usePlayer = true;
		
		StringBuilder sb = new StringBuilder();
		sb.append("Vote Recieved| Timestamp:").append(vote.getTimeStamp()).append(" Address:").append(vote.getAddress()).append(" ServiceName:").append(vote.getServiceName()).append(" Username: ").append(vote.getUsername());
		
		log.info(sb.toString());
		
		final String user = vote.getUsername();
		if (event.isAsynchronous() && usePlayer)
		{
			CommonUtil.nextTick(new Runnable() {
				
				public void run() {
					NoxPlayer player = manager.getPlayer(Bukkit.getServer().getOfflinePlayer(user));
					
					synchronized (player) {
						player.incrementVote();
					}
				}
			});
		} else {
			NoxPlayer player = manager.getPlayer(vote.getUsername());
			
			synchronized (player) {
				player.incrementVote();
			}
		}
	}
}
