package com.noxpvp.core.tp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface WarpPoint {
	public boolean tryTeleport(Entity entity);
	
	public boolean canTeleport(Entity entity);

	public void teleport(Entity entity);
	
	public Location getLocation();
}
