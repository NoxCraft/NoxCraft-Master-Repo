package net.noxcraft.core.tp;

import org.bukkit.entity.Entity;

public interface WarpPoint {
	public boolean tryTeleport(Entity entity);
	
	public boolean canTeleport(Entity entity);

	public void teleport(Entity entity);

}
