package com.noxpvp.core.external.towny;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.bases.mutable.LocationAbstract;

public interface ITownyHook {
	public boolean isTownyEnabled();
	
	public boolean isUsingTowny(World world);

	public boolean isUsingTowny(String worldNamed);
	
	public boolean isClaimedLand(LocationAbstract location);
	
	public boolean isClaimedLand(Location location);
	
	public boolean isWild(LocationAbstract location);
	
	public boolean isWild(Location location);
	
	public boolean isOwnLand(Player p);
	
	public boolean isOwnLand(Player p, LocationAbstract location);
	
	public boolean isOwnLand(Player p, Location location);
	
	public boolean isTownMember(Player player, Location location);
	
	public boolean isTownMember(Player player, String townName);
	
	public boolean isTownMember(String playerName, String townName);
	
	public boolean isPVP(Entity entity);

	public boolean isPVP(LocationAbstract loc);
	
	public boolean isPVP(Location loc);
	
	public boolean isAlly(Player player, Entity entity);
	
	public boolean isAlly(Player player, LocationAbstract location);
	
	public boolean isAlly(Player player, Location Location);
	
	public boolean isAlly(Player player, Player otherPlayer);
	
	public boolean isNationMember(Player player, Entity entity);
	
	public boolean isNationMember(Player player, LocationAbstract location);
	
	public boolean isNationMember(Player player, Location location);
	
	public boolean isNationMember(Player player, Player otherPlayer);
}
