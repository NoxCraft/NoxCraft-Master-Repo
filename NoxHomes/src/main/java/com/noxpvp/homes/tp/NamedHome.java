package com.noxpvp.homes.tp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NamedHome extends BaseHome {
	private String name;
	
	public static final String PERM_NODE = "named";
	
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
	protected String getNode() {
		return super.getNode() + '.' + PERM_NODE;
	}
	
	@Override
	protected String getOtherNode() {
		return super.getOtherNode() + '.' + PERM_NODE;
	}

	@Override
	public String getName() {
		return name;
	}
}
