package com.noxpvp.core.tp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface WarpPoint {
	public boolean canTeleport(Entity entity, boolean multi);

	public Location getLocation();

	public void teleport(Entity entity, boolean multi);

	public boolean tryTeleport(Entity entity, boolean multi);
}
