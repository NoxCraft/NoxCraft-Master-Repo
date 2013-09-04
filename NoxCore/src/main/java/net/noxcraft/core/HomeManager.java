package net.noxcraft.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.noxcraft.core.tp.BaseHome;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;

public class HomeManager implements Persistant {
	private Map<String, List<BaseHome>>home_data;
	private File configFile;
	private FileConfiguration config;
	
	protected NoxCore plugin;
	
	public HomeManager(NoxCore plugin)
	{
		this.plugin = plugin;
		home_data = new HashMap<String, List<BaseHome>>(plugin.getServer().getMaxPlayers()*2);
		configFile = plugin.getDataFile("homes.yml");
		config = new FileConfiguration(configFile);
		config.setIndent(4);
	}
	
	public void load()
	{
			config.load();
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
	
	public void unloadAndSave(String name)
	{
		unload(name);
		config.save();
	}
}
