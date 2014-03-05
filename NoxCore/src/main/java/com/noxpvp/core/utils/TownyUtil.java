package com.noxpvp.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.SafeLocation;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.db.TownyDataSource;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.TownyWorld;

public class TownyUtil {
	private static ModuleLogger log;
	
	private static Towny towny;
	
	public static void setup() {
		if (log == null)
			log = NoxCore.getInstance().getModuleLogger("TownyUtil");
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Towny");
		if (plugin instanceof Towny)
			towny = (Towny) plugin;
		else if (plugin != null)
			log.severe("NoxCore is outdated. Towny no longer of the class type Towny?");
		else
			log.info("Towny was not found.");
	}

	/**
	 * Should not instance this object. Meant to be static.
	 */
	public TownyUtil() {
		super();
	}
	
	public static boolean isTownyEnabled() {
		if (towny == null)
			return false;
		return towny.isEnabled();
	}
	
	public static boolean isUsingTowny(World world) {
		return isUsingTowny(world.getName());
	}

	public static boolean isUsingTowny(String worldNamed) {
		if (!isTownyEnabled())
			return false;
		
		try {
			return TownyUniverse.getDataSource().getWorld(worldNamed).isUsingTowny();
		} catch (NotRegisteredException e) {
			return false;
		}
	}
	
	public static boolean isClaimedLand(SafeLocation location) {
		return isClaimedLand(location.toLocation());
	}
	
	public static boolean isClaimedLand(Location location) {
		if (location == null)
			return false; // To location may be null?
		
		if (!isTownyEnabled())
			return false;
		if (!isUsingTowny(location.getWorld()))
			return false;
		
		TownyWorld world = null;
		try {
			world = TownyUniverse.getDataSource().getWorld(location.getWorld().getName());
		} catch (NotRegisteredException e) {}

		if (world != null)
			return world.hasTownBlock(Coord.parseCoord(location));
		else return false;
	}
	
	public static boolean isWild(SafeLocation location){
		return isWild(location.toLocation());
	}
	
	public static boolean isWild(Location location){
		if (!isTownyEnabled()) //Towny not loaded.
			return false; //Always return false
		
		if (!isUsingTowny(location.getWorld())) //Overrides the using towny since the thing returns negated stuff.
			return false; //Towny not used. Always return false.
		
		return !isClaimedLand(location); //Negated due to wild. 
	}
	
	public static boolean isOwnLand(Player p, SafeLocation location){
		return isOwnLand(p, location.toLocation());
	}
	
	public static boolean isOwnLand(Player p, Location location) {
		
		if (!isTownyEnabled()) //Towny not loaded.
			return false; //Always return false
		
		if (!isUsingTowny(location.getWorld())) //Overrides the using towny since the thing returns negated stuff.
			return false; //Towny not used. Always return false.

		Resident res = null;
		TownyWorld world = null;
		try {
			TownyDataSource source = TownyUniverse.getDataSource();
			
			res = source.getResident(p.getName());
			world = source.getWorld(location.getWorld().getName());
		} catch (NotRegisteredException e) {}
		
		if (res == null || world == null)
			return false;
		
		try {
			if (world.getTownBlock(Coord.parseCoord(location)).getTown().hasResident(res))
				return true;
		} catch (NotRegisteredException e) {}
		
		return false;
	}
	
	public static boolean isTownMember(Player player, Location location) {
		Town town = getTown(location);
		if (town == null)
			return false;
		
		return town.hasResident(player.getname());
	}
	
	public static boolean isTownMember(Player player, String townName) {
		return isTownMember(player.getName(), townName);
	}
	
	public static boolean isTownMember(String playerName, String townName) {
		Town town = getTown(townName);
		if (town == null)
			return false;
		
		return town.hasResident(playerName);
	}
	
	public static boolean isPVP(Entity entity) {
		return isPVP(entity.getLocation());
	}

	public static boolean isPVP(SafeLocation loc){
		return isPVP(loc.toLocation());
	}
	
	public static boolean isPVP(Location loc){
		if (!isTownyEnabled()) //Towny not loaded.
			return false; //Always return false
		
		if (!isUsingTowny(loc.getWorld())) //Overrides the using towny since the thing returns negated stuff.
			return false; //Towny not used. Always return false.
		
		TownyWorld world = null;
		try {
			world = TownyUniverse.getDataSource().getWorld(loc.getWorld().getName());
			
			if (world != null){
				return world.getTownBlock(Coord.parseCoord(loc)).getTown().isPVP();
			}
			
		} catch (NotRegisteredException e) {
			if (world != null)
				return world.isPVP();
		}
		
		return false;
	}
	
	private static Town getTown(String townName) {
		try {
			return TownyUniverse.getDataSource().getTown(townName);
		} catch (NotRegisteredException e) {
			return null;
		}
	}
	
	private static Town getTown(Location location) {
		Town town = null;
		try {
			Coord coord = Coord.parseCoord(location);
			TownyWorld world = TownyUniverse.getDataSource().getWorld(location.getWorld().getName());
			
			if (world != null)
				town = world.getTownBlock(coord).getTown();
		} catch (NotRegisteredException e) { }
		return town;
	}

	/*
	 * is ally land(block / loc)
	 */
}
