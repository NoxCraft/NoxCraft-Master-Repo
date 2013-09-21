package com.noxpvp.homes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;


import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;

public class HomeManager implements Persistant { //FIXME: Javadocs
	private Map<String, List<BaseHome>>home_data;
	private Set<String> players = new HashSet<String>();
	private File configFile;
	private FileConfiguration config;
	
	protected NoxCore plugin;
	
	public HomeManager()
	{
		this.plugin = NoxCore.getInstance();
		home_data = new HashMap<String, List<BaseHome>>(plugin.getServer().getMaxPlayers()*2);
		configFile = plugin.getDataFile("homes.yml");
		config = new FileConfiguration(configFile);
		config.setIndent(4);
	}
	
	public List<BaseHome> getHomes(OfflinePlayer player)
	{
		return getHomes(player.getName());
	}
	
	public List<BaseHome> getHomes(String player)
	{
		if (home_data.containsKey(player))
			return Collections.unmodifiableList(home_data.get(player));
		else
			return Collections.unmodifiableList(new ArrayList<BaseHome>(0));
	}
	
	public boolean isLoaded(OfflinePlayer player)
	{
		return isLoaded(player.getName());
	}
	
	public boolean isLoaded(String player)
	{
		return (home_data.containsKey(player));
	}
	
	public boolean hasData(OfflinePlayer player)
	{
		return hasData(player.getName());
	}
	
	public boolean hasData(String player)
	{
		return players.contains(player);
	}
	
	public void load()
	{
		players.clear();
		config.load();
		for (String name : config.getKeys())
			players.add(name);
	}
	
	public void loadHomes(OfflinePlayer player)
	{
		loadHomes(player.getName());
	}
	
	public void loadHomes(String player)
	{
		
	}
	
	public void save()
	{
		for (String key : home_data.keySet())
		{
			List<BaseHome> homes = home_data.get(key);
			ConfigurationNode node = config.getNode(key);
			if (homes != null && !homes.isEmpty())
			{
				for (BaseHome home : homes)
				{
					String homeName = home.getName();
					if (!home.isOwner(key))
						continue;
					
					node.set(homeName, home);
				}
			}
			else
				config.remove(key);
		}
		config.save();
	}
	
	public void unload(OfflinePlayer player)
	{
		unload(player.getName());
	}
	
	public void unload(String name)
	{
		if (home_data.containsKey(name))
		{
			List<BaseHome> homes = home_data.get(name);
			ConfigurationNode node = config.getNode(name);
			if (homes != null && !homes.isEmpty())
			{
				for (BaseHome home : homes)
				{
					String homeName = home.getName();
					if (!home.isOwner(name))
						continue;
					
					node.set(homeName, home);
				}
			}
			else
				config.remove(name);
			home_data.remove(name);
		}
	}
	
	public void unloadAndSave(OfflinePlayer player)
	{
		unloadAndSave(player.getName());
	}
	
	public void unloadAndSave(String name)
	{
		unload(name);
		config.save();
	}

	public void addHomes(List<BaseHome> homes) {
		if (LogicUtil.nullOrEmpty(homes))
			return;
		
		for (BaseHome home : homes)
			addHome(home);
	}
	
	public void addHome(BaseHome home)
	{
		String owner = home.getOwner();
		if (home_data.containsKey(owner))
			home_data.get(owner).add(home);
		else
		{
			home_data.put(owner, new ArrayList<BaseHome>());
			home_data.get(owner).add(home);
		}
	}
	
	public boolean removeHome(BaseHome home)
	{
		String owner = home.getOwner();
		
		if (home_data.containsKey(owner))
		{
			home_data.get(owner).remove(home);
			config.getNode(owner).remove(home.getName());
			return true;
		}
		else
			return false;
	}
	
	public BaseHome getHome(String owner, String name)
	{
		if (!home_data.containsKey(owner))
			return null;
		
		List<BaseHome> homes = getHomes(owner);
		for (BaseHome home : homes)
			if (home instanceof DefaultHome && name == null)
				return home;
			else if (home.getName().equals(name))
				return home;
		return null;
	}
	
	public void clear() {
		home_data.clear();
		players.clear();
		config.clear();
	}
}
