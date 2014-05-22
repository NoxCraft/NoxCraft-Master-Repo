package com.noxpvp.mmo.runnables;

import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class UnregisterMMOHandlerRunnable extends BukkitRunnable {

	private BaseMMOEventHandler<? extends Event> handler;

	public UnregisterMMOHandlerRunnable(BaseMMOEventHandler<? extends Event> handler) {
		this.handler = handler;
	}

	public void run() {
		NoxMMO.getInstance().getMasterListener().unregisterHandler(handler);
	}

}
