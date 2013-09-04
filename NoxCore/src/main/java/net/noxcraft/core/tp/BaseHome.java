package net.noxcraft.core.tp;

import net.noxcraft.core.SafeLocation;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.internal.CommonPlugin;
import com.bergerkiller.bukkit.common.internal.PermissionHandler;

public abstract class BaseHome implements WarpPoint {
	
	public static final String HOME_NODE		= "noxcraft.homes.home";
	public static final String OTHER_HOME_NODE	= "noxcraft.homes.others.home";
	
	protected String owner;
	protected final PermissionHandler permHandler;
	protected SafeLocation warpPoint;
	
	public BaseHome(String owner, Location location) {
		permHandler = CommonPlugin.getInstance().getPermissionHandler();
		this.owner = owner;
	}
	
	public BaseHome(String owner, Entity e)
	{
		this(owner, e.getLocation());
	}
	
	public BaseHome(Player player)
	{
		this(player.getName(), player.getLocation());
	}
	
	public boolean tryTeleport(Entity entity) {
		if (canTeleport(entity))
			teleport(entity);
		else
			return false;
		return true;
	}
	
	protected String getNode() {
		return HOME_NODE;
	}
	
	protected String getOtherNode() {
		return OTHER_HOME_NODE;
	}
	public boolean canTeleport(Player player)
	{
		if (isOwner(player))
			return permHandler.hasPermission(player, getNode());
		else
			return permHandler.hasPermission(player, getOtherNode());
	}

	public boolean canTeleport(Entity entity) {
		if (entity instanceof Player)
			return canTeleport((Player)entity);
		return false;
	}

	public void teleport(Entity entity) {
		entity.teleport(warpPoint.toLocation());
	}
	
	public boolean isOwner(Player player)
	{
		return isOwner(player.getName());
	}
	
	public boolean isOwner(String owner)
	{
		return this.owner.equals(owner);
	}

	public abstract String getName();
	
}
