package com.noxpvp.homes.tp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NamedHome extends BaseHome {
	private String name;
	
	public NamedHome(Player player, String name) {
		super(player);
		this.name = name;
	}

	public NamedHome(String owner, String name, Entity e) {
		super(owner, e);
		this.name = name;
	}

	public NamedHome(String owner, String name, Location location) {
		super(owner, location);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
