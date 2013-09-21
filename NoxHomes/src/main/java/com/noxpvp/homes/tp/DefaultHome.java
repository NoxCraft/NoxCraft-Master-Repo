package com.noxpvp.homes.tp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DefaultHome extends BaseHome {
	public final static String DEF_NAME = "default";
	
	@Override
	protected String getNode() {
		return super.getNode() + '.' + DEF_NAME;
	}
	
	@Override
	protected String getOtherNode() {
		return super.getOtherNode() + '.' + DEF_NAME;
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
		return DEF_NAME;
	}
}
