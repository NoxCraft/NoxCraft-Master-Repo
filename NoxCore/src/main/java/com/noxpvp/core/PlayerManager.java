package com.noxpvp.core;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.data.NoxPlayer;

public class PlayerManager implements Persistant {

	private FileConfiguration config;
	
	private Map<String, NoxPlayer> players;
	
	public void save() {
		config.save();
	}

	public void load() {
		Collection<String> pls = players.keySet();

		players.clear();
		config.load();
		
		for (String name : pls)
			loadOrCreate(name);
		
		for (Player player : Bukkit.getOnlinePlayers())
			loadOrCreate(player.getName());
	}
	
	public ConfigurationNode getPlayerNode(String name)
	{
		if (isMultiFile())
			return new FileConfiguration(NoxCore.getInstance(), "playerdata"+File.pathSeparator+name+".yml");
		else if (config != null)
			return config.getNode("players").getNode(name);
		else
			return null;
	}
	
	public NoxPlayer getPlayer(OfflinePlayer player) {
		return getPlayer(player.getName());
	}
	
	public NoxPlayer getPlayer(String name)
	{
		if (players.containsKey(name))
			return players.get(name);
		else
		{
			loadOrCreate(name);
			return players.get(name);
		}
	}
	
	private NoxPlayer loadOrCreate(String name) {
		NoxPlayer player = null;
		
		if (players.containsKey(name))
			player = players.get(name);
		else
			new NoxPlayer(this, name);

		player.load();
		
		if (!players.containsKey(name))
			players.put(name, player);
		
		return player;
	}
	
	public boolean isPlayerInMemeory(String name) {
		return players.containsKey(name);
	}
	
	public void savePlayer(String name) {
		savePlayer(getPlayer(name));
	}

	public void savePlayer(OfflinePlayer player){
		savePlayer(getPlayer(player));
	}
	
	public void savePlayer(NoxPlayer player) 
	{
		player.save();
	}
	
	public void loadPlayer(NoxPlayer noxPlayer) {
		noxPlayer.load();
	}

	public void loadPlayer(String name) {
		if (!players.containsKey(name))
		{
			NoxPlayer player = loadOrCreate(name);
			loadPlayer(player);
		}
	}
	
	

	
	
//////// HELPER FUNCTIONS
	public boolean isMultiFile() { return NoxCore.isUseUserFile(); }
	
	public File getPlayerFile(String name)
	{
		return NoxCore.getInstance().getDataFile("playerdata", name);
	}
	
	public File getPlayerFile(NoxPlayer noxPlayer) {
		return getPlayerFile(noxPlayer.getName());
	}
}
