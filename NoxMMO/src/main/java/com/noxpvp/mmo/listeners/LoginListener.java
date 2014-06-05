package com.noxpvp.mmo.listeners;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.gui.HealthBar;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener extends NoxListener<NoxMMO> {

	public LoginListener(NoxMMO plugin) {
		super(plugin);
	}

	public LoginListener() {
		super(NoxMMO.getInstance());
	}

	public void onLogin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		new HealthBar(player);
	}

}
