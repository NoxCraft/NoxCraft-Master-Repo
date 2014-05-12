package com.noxpvp.core.external.towny;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.bases.mutable.LocationAbstract;

public class NullTownyHook implements ITownyHook {

	public boolean isTownyEnabled() {
		return false;
	}

	public boolean isUsingTowny(World world) {
		return false;
	}

	public boolean isUsingTowny(String worldNamed) {
		return false;
	}

	public boolean isClaimedLand(LocationAbstract location) {
		return false;
	}

	public boolean isClaimedLand(Location location) {
		return false;
	}

	public boolean isWild(LocationAbstract location) {
		return true;
	}

	public boolean isWild(Location location) {
		return true;
	}

	public boolean isOwnLand(Player p) {
		return false;
	}

	public boolean isOwnLand(Player p, LocationAbstract location) {
		return false;
	}

	public boolean isOwnLand(Player p, Location location) {
		return false;
	}

	public boolean isTownMember(Player player, Location location) {
		return false;
	}

	public boolean isTownMember(Player player, String townName) {
		return false;
	}

	public boolean isTownMember(String playerName, String townName) {
		return false;
	}

	public boolean isPVP(Entity entity) {
		return isPVP(entity.getLocation());
	}

	public boolean isPVP(LocationAbstract loc) {
		return isPVP(loc.toLocation());
	}

	public boolean isPVP(Location loc) {
		return loc.getWorld().getPVP();
	}

	public boolean isAlly(Player player, Entity entity) {
		return false;
	}

	public boolean isAlly(Player player, LocationAbstract location) {
		return false;
	}

	public boolean isAlly(Player player, Location Location) {
		return false;
	}

	public boolean isAlly(Player player, Player otherPlayer) {
		return false;
	}

	public boolean isNationMember(Player player, Entity entity) {
		return false;
	}

	public boolean isNationMember(Player player, LocationAbstract location) {
		return false;
	}

	public boolean isNationMember(Player player, Location location) {
		return false;
	}

	public boolean isNationMember(Player player, Player otherPlayer) {
		return false;
	}

}
