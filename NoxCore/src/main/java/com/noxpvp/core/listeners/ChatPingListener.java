package com.noxpvp.core.listeners;
 
import java.util.ArrayList;
import java.util.List;
 


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.events.ChatPingEvent;
 
public class ChatPingListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChatEvent(AsyncPlayerChatEvent event){
		final String msg = event.getMessage();
		final List<Player> players = new ArrayList<Player>();
		for (final Player p: Bukkit.getServer().getOnlinePlayers())
		{
			if (msg.contains("@"+p.getName()))
			{
				players.add(p);
			}
		}
		
		
		Bukkit.getServer().getScheduler().runTaskLater(NoxCore.getInstance(), new Runnable() {
			public void run() {
				for (Player p : players){
					ChatPingEvent ping = CommonUtil.callEvent(new ChatPingEvent(p, false));
					if (!ping.isCancelled())
						p.playSound(p.getLocation(), Sound.NOTE_PIANO, 100, 0);
				}
			}
		}, 0);
		
		if (players.isEmpty())
			return;
		
		List<String> list = new ArrayList<String>();
		List<String> newNames = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(new String(msg));
		String lastCol = ChatColor.getLastColors(sb.toString());
		
		final String reset;
		if (lastCol != null && !lastCol.equals(""))
			reset = ChatColor.COLOR_CHAR + "r" + lastCol;
		else
			reset = ChatColor.COLOR_CHAR + "r";
				
		final String ital = ChatColor.ITALIC.toString();
		final String green = ChatColor.GREEN.toString();
		int i = 0;
		
		for(Player p : players) {list.add(i, "@" + p.getName()); newNames.add(i++, green + ital + p.getName() + reset);}
			
		int n = 0;
		for (String s: list) {
			while(sb.indexOf(s, 0) >= 0){
				sb.replace((sb.indexOf(s, 0)), (sb.indexOf(s, 0) + s.length()), newNames.get(n));
			}
			n++;
		}
		event.setMessage(sb.toString());
	}
}