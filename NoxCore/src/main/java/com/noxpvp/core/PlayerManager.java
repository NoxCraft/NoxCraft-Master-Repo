/*
 * 
 */
package com.noxpvp.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
	
	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#save()
	 */
	public void save() {
		config.save();
	}

	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#load()
	 */
	public void load() {
		Collection<String> pls = players.keySet();

		players.clear();
		config.load();
		
		for (String name : pls)
			loadOrCreate(name);
		
		for (Player player : Bukkit.getOnlinePlayers())
			loadOrCreate(player.getName());
	}
	
	/**
	 * Gets the player node.
	 * <b>INTERNAL METHOD</b> Best not to use this!
	 * @param name the name
	 * @return the player node
	 */
	public ConfigurationNode getPlayerNode(String name)
	{
		if (isMultiFile())
			return new FileConfiguration(NoxCore.getInstance(), "playerdata"+File.pathSeparator+name+".yml");
		else if (config != null)
			return config.getNode("players").getNode(name);
		else
			return null;
	}
	
	/**
	 * Gets the player.
	 *
	 * @see #getPlayer(String)
	 * @param player the player
	 * @return the player
	 */
	public NoxPlayer getPlayer(OfflinePlayer player) {
		return getPlayer(player.getName());
	}
	
	
	/**
	 * Gets the specified player.
	 *
	 * @param name of the player
	 * @return the player
	 */
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
	
	/**
	 * Unload if player is offline.
	 * <br/>
	 * <b> WARNING IF THERE ARE PLUGINS THAT ARE IMPROPERLY CACHING THIS OBJECT. IT WILL NEVER TRUELY UNLOAD</b>
	 * @param name of the player
	 * @return true, if it unloads the player from memory.
	 */
	public boolean unloadIfOffline(String name) {
		if (isPlayerInMemory(name) && getPlayer(name).getPlayer() != null)
		{
			unloadAndSavePlayer(name);
			return true;
		}
		return false;
	}
	
	/**
	 * Unload and save player.
	 * 
	 * @see #unloadPlayer(String)
	 * @see #savePlayer(String)
	 * @param name the name
	 */
	public void unloadAndSavePlayer(String name) {
		getPlayer(name).save();
		unloadPlayer(name);
	}

	/**
	 * Unload the named player.
	 *
	 * @param name of the player
	 */
	public void unloadPlayer(String name) {
		players.remove(name);
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
	
	/**
	 * Checks if is player is in memory.
	 *
	 * @param name of the player.
	 * @return true, if is player in memory
	 */
	public boolean isPlayerInMemory(String name) {
		return players.containsKey(name);
	}
	
	/**
	 * Save player.
	 *
	 * @see #savePlayer(NoxPlayer)
	 * @param name of the player
	 */
	public void savePlayer(String name) {
		savePlayer(getPlayer(name));
	}

	/**
	 * Save player.
	 *
	 * @see #savePlayer(NoxPlayer)
	 * @param player of the player
	 */
	public void savePlayer(OfflinePlayer player){
		savePlayer(getPlayer(player));
	}
	
	/**
	 * Save player.
	 *
	 * @param player the player
	 */
	public void savePlayer(NoxPlayer player) 
	{
		player.save();
	}
	
	/**
	 * Load player.
	 *
	 * @param noxPlayer the NoxPlayer object to load
	 */
	public void loadPlayer(NoxPlayer noxPlayer) {
		noxPlayer.load();
	}
	
	

	/**
	 * Load player.
	 *
	 * @param name of the player
	 */
	public void loadPlayer(String name) {
		if (!players.containsKey(name))
		{
			NoxPlayer player = loadOrCreate(name);
			loadPlayer(player);
		}
	}
	
	

	
	
//////// HELPER FUNCTIONS
	/**
	 * Checks if is multi file.
	 * <b>INTERNALLY USED METHOD</b>
	 * @return true, if is using the multi file structure for player data.
	 */
	public boolean isMultiFile() { return NoxCore.isUseUserFile(); }
	
	/**
	 * Gets the player file.
	 *
	 * @param name of the player
	 * @return the player file
	 */
	public File getPlayerFile(String name)
	{
		return NoxCore.getInstance().getDataFile("playerdata", name);
	}
	
	/**
	 * Gets the player file.
	 *
	 * @see #getPlayerFile(String)
	 * @param noxPlayer the NoxPlayer object
	 * @return the player file
	 */
	public File getPlayerFile(NoxPlayer noxPlayer) {
		return getPlayerFile(noxPlayer.getName());
	}

	public List<String> getAllPlayerNames() {
		List<String> ret = new ArrayList<String>();
		if (isMultiFile())
		{
			for (File f : NoxCore.getInstance().getDataFile("playerdata").listFiles())
				ret.add(f.getName().replace(".yml", ""));
		} else {
			for (ConfigurationNode node : config.getNode("players").getNodes())
				ret.add(node.getName());
		}
		
		return Collections.unmodifiableList(ret);
	}
}
