package com.noxpvp.core.listeners;
 
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.events.ChatPingEvent;
import com.noxpvp.core.utils.MessageUtil;

public class ChatPingListener extends NoxListener<NoxCore> {
	
	private String color;
	private String chatChar;
	
	public ChatPingListener()
	{
		super(NoxCore.getInstance());
		
		ConfigurationNode localNode = getPlugin().getLocalizationNode("Special.ChatPing");
		
		this.color = MessageUtil.parseColor(localNode.get("Color", "&2"));
		this.chatChar = localNode.get("Chat-Character", "@");
	}
	
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
	public void onChatPingTabComplete(PlayerChatTabCompleteEvent event){
		String token = event.getLastToken();
		
		if (!token.startsWith(chatChar)) return;
		
		String tempToken = token.substring(token.indexOf(chatChar));
		Player tabber = event.getPlayer();
		
		for (Player p : CommonUtil.getOnlinePlayers()){
			String name = p.getName();
			
			if (name.toLowerCase().startsWith(tempToken.toLowerCase()) && tabber.canSee(p))
				event.getTabCompletions().add(token + name.substring(name.indexOf(tempToken)));//yes this works, says someone on bukkit
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChatEvent(AsyncPlayerChatEvent event){
		final String msg = event.getMessage();
		final List<Player> players = new ArrayList<Player>();
		for (final Player p: CommonUtil.getOnlinePlayers())
		{
			if (msg.contains(chatChar + p.getName()))
			{
				players.add(p);
			}
		}
		
		
		Bukkit.getServer().getScheduler().runTaskLater(NoxCore.getInstance(), new Runnable() {
			public void run() {
				for (Player p : players){
					ChatPingEvent ping = CommonUtil.callEvent(new ChatPingEvent(p, false));
					if (!ping.isCancelled())
						p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 100, 0); // this is the long distance bow hit marker, much louder
				}
			}
		}, 0);
		
		if (players.isEmpty())
			return;
		
		List<String> list = new ArrayList<String>();
		List<String> newNames = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(new String(msg));
		String lastCol = ChatColor.getLastColors(sb.toString());
		
		final String reset = lastCol;
		
		int i = 0;
		
		for(Player p : players) {
			list.add(i, chatChar + p.getName());
			newNames.add(i++, color + p.getName() + reset);
			
		}
			
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