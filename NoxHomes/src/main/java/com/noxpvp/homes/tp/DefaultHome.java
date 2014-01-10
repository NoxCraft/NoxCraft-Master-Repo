package com.noxpvp.homes.tp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DefaultHome extends BaseHome {
	public static final String PERM_NODE = "default";
	
	@Override
	protected String getNode() {
		return super.getNode() + '.' + PERM_NODE;
	}
	
	@Override
	protected String getOtherNode() {
		return super.getOtherNode() + '.' + PERM_NODE;
	}
	
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
		return PERM_NODE;
	}
}
