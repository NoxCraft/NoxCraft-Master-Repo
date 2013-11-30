/*
 * 
 */
package com.noxpvp.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.data.CoreBar;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.CoreBoard;

public class PlayerManager implements Persistant {

	protected FileConfiguration config;
	
	private Map<String, CoreBar> coreBars = new HashMap<String, CoreBar>();
	private Map<String, CoreBoard> coreBoards = new HashMap<String, CoreBoard>();
	private Map<String, NoxPlayer> players;
	
	public PlayerManager() {
		this(new FileConfiguration(NoxCore.getInstance().getDataFile("players.yml")));
		config = new FileConfiguration(NoxCore.getInstance().getDataFile("players.yml"));
		players = new HashMap<String, NoxPlayer>();
	}
	
	public PlayerManager(FileConfiguration conf)
	{
		this.config = conf;
		players = new HashMap<String, NoxPlayer>();
	}
	
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
			return new FileConfiguration(NoxCore.getInstance(), "playerdata"+File.separatorChar+name+".yml");
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
			player = new NoxPlayer(this, name);
		
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
	
	/**
	 * Gets all the currently active coreBoards
	 * 
	 * @return Collection<CoreBoard> The CoreBoards
	 */
	public Collection<CoreBoard> getCoreBoards(){
		return this.coreBoards.values();
	}
	
	/**
	 * 
	 * @param name The Key
	 * @return CoreBoard The CoreBoard
	 * @throws NullPointerException If the key is null
	 */
	public CoreBoard getCoreBoard(String name){
		if (name == null)
			throw new NullPointerException("Cannot use with null key");
			
		return this.coreBoards.get(name);
	}
	
	/**
	 * 
	 * @param name The Key
	 * @return boolean If there is a CoreBoard active with the specific key
	 * @throws NullPointerException If the key is null
	 */
	public boolean hasCoreBoard(String name){
		if (name == null)
			throw new NullPointerException("Cannot check with null key");
			
		return this.coreBoards.containsKey(name);
	}
	
	/**
	 * 
	 * @param name The Key
	 * @param board The CoreBoard to add
	 * @throws NullPointerException If the key or CoreBoard are null
	 */
	public void addCoreBoard(CoreBoard board){
		if (board == null)
			throw new NullPointerException("Cannot CoreBoard to active CoreBoard list");
		
		this.coreBoards.put(board.p.getName(), board);
	}
	
	/**
	 * 
	 * 
	 * @param name The Key
	 * @return CoreBar The CoreBar
	 * @throws NullPointerException If the key is null
	 */
	public CoreBar getCoreBar(String name){
		if (name == null)
			throw new NullPointerException("Cannot use with null key");
		
		return this.coreBars.containsKey(name) ? this.coreBars.get(name) : new CoreBar(NoxCore.getInstance(), Bukkit.getPlayer(name));
	}
	
	/**
	 * 
	 * @param name The Key
	 * @return boolean If there is a CoreBar active with the specific key
	 * @throws NullPointerException If the key is null
	 */
	public boolean hasCoreBar(String name) {
		if (name == null)
			throw new NullPointerException("Cannot check with null key");
		
		return this.coreBars.containsKey(name);
	}
	
	/**
	 * 
	 * @param name The Key
	 * @param bar The CoreBar to add
	 * @throws NullPointerException If the key or CoreBar are null
	 */
	public void addCoreBar(CoreBar bar){
		if (bar == null)
			throw new NullPointerException("Cannot CoreBar to active CoreBar list");
		
		this.coreBars.put(bar.p.getName(), bar);
	}
	
	/**
	 * 
	 * @param name The key for the CoreBar to remove
	 * @return PlayerManager This instance
	 * @throws NullPointerException If the key is null
	 */
	public PlayerManager removeCoreBar(String name){
		if (name == null)
			throw new NullPointerException("Cannot remove null Key from list");
		
		this.coreBars.remove(name); return this;
	}
	
	/** 
	 * @param name The key for the CoreBoard to remove
	 * @return PlayerManager This instance
	 * @throws NullPointerException If the key is null
	 */
	public PlayerManager removeCoreBoard(String name){
		if (name == null)
			throw new NullPointerException("Cannot remove null Key from list");
		
		this.coreBoards.remove(name); return this;
	}
}
