package com.noxpvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.events.PlayerDataSaveEvent;

public class DataListener extends NoxListener<NoxCore> {
	public DataListener() {
		this(NoxCore.getInstance());
	}

	public DataListener(NoxCore instance) {
		super(instance);
	}
	
	@Override
	public void register() {
		super.register();
		CommonUtil.queueListenerLast(this, PlayerDataSaveEvent.class);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSave(PlayerDataSaveEvent event)
	{
		if (!event.shouldHonorCore())
			return;
		
		event.getPlayer().getNoxPlayer().save(false);
	}
}
