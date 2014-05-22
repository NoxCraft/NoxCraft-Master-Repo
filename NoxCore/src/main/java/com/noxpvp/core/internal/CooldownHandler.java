package com.noxpvp.core.internal;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.events.CooldownExpireEvent;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.manager.CorePlayerManager;

public class CooldownHandler extends BukkitRunnable {
	private Map<String, List<CoolDown>> cds;
	private NoxCore core;

	public CooldownHandler() {
		cds = new MapMaker().concurrencyLevel(2).makeMap();
		core = NoxCore.getInstance();
	}

	public synchronized void loadPlayer(NoxPlayerAdapter adapter) {
		NoxPlayer player = adapter.getNoxPlayer();

		cds.put(player.getName(), player.getCoolDowns());
	}

	public void loadPlayer(OfflinePlayer player) {
		loadPlayer(CorePlayerManager.getInstance().getPlayer(player));
	}

	public void loadPlayer(String name) {
		loadPlayer(CorePlayerManager.getInstance().getPlayer(name));
	}

	public void run() {
		for (Entry<String, List<CoolDown>> entry : cds.entrySet()) {
			final String name = entry.getKey();
			for (final CoolDown cd : entry.getValue())
				if (cd.expired()) {
					CommonUtil.nextTick(new Runnable() {
						public void run() {
							Player p = Bukkit.getPlayer(name);
							if (p != null)
								CommonUtil.callEvent(new CooldownExpireEvent(p, cd.getName()));
						}
					});
				}
		}
	}

	public synchronized void start() {
		try {
			runTaskTimerAsynchronously(core, 0, 20);
		} catch (Throwable e) {
			core.getLogger().severe("Failed to initialize cooldown manager timer.");
			e.printStackTrace();
		}
	}

	public synchronized void stop() {
		try {
			cancel();
		} catch (Throwable e) {
			if (e instanceof IllegalStateException)
				return;
			core.getLogger().warning("Failed to stop the cooldown manager timer.");
			e.printStackTrace();
		}
	}
}
