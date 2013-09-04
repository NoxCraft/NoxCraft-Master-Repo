package net.noxcraft.core.tp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DefaultHome extends BaseHome {
	
	public DefaultHome(Player player) {
		super(player);
	}

	public DefaultHome(String owner, Entity e) {
		super(owner, e);
	}

	public DefaultHome(String owner, Location location) {
		super(owner, location);
	}
	
	@Override
	public String getName() {
		return "default";
	}
}
