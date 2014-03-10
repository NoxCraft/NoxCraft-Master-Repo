package com.noxpvp.core.listeners;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;

import com.noxpvp.core.NoxCore;

public class ServerPingListener extends NoxListener<NoxCore>{

	private List<String> motds;
	
	public ServerPingListener(NoxCore core) {
		super(core);
		
		this.motds = core.getCoreConfig().getList("motd.messages", String.class);
		System.out.println(motds.toString());
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPing(ServerListPingEvent event){
		if (motds == null || motds.size() < 1) return;
		
		int random = RandomUtils.nextInt(motds.size());
		
		if (motds.get(random) != null)
			event.setMotd(motds.get(random));
	}
}
