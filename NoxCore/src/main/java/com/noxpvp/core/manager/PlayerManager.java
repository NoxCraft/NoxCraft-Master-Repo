/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.core.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.internal.LockerCaller;
import com.noxpvp.core.internal.SafeLocker;
import com.noxpvp.core.utils.UUIDUtil;

public class PlayerManager extends BasePlayerManager<NoxPlayer> implements Persistant, LockerCaller, SafeLocker {
	
	private static PlayerManager instance;
	private static List<IPlayerManager<?>> managers = new ArrayList<IPlayerManager<?>>();
	
	private AtomicBoolean isLocked = new AtomicBoolean();
	private LockerCaller lockCaller = null;
	
	public static void addManager(IPlayerManager<?> manager) {
		if (PlayerManager.managers.contains(manager))
			return;
		
		PlayerManager.managers.add(manager);
	}
	
	@Override
	public boolean unloadIfOffline(String name) {
		boolean unloaded = super.unloadIfOffline(name);
		if (unloaded)
			unloadPlayer(name);
		return unloaded;
	}
	
	@Override
	public void unloadPlayer(String name) {
		super.unloadPlayer(name);
		for (IPlayerManager<?> manager : managers)
			if (manager != this)
				manager.unloadPlayer(name);
	}
	
	
	public static PlayerManager getInstance() {
		if (instance == null)
			instance = new PlayerManager();
		
		return instance;
	}
	
	public static PlayerManager getInstance(FileConfiguration conf, NoxCore plugin)
	{
		if (instance == null)
			instance = new PlayerManager(conf, plugin);
		
		return instance;
	}
	
	protected FileConfiguration config;
	
	private NoxCore plugin;
	
	protected PlayerManager() {
		this(new FileConfiguration(NoxCore.getInstance().getDataFile("players.yml")), NoxCore.getInstance());
		config = new FileConfiguration(NoxCore.getInstance().getDataFile("players.yml"));
	}

	protected PlayerManager(FileConfiguration conf, NoxCore plugin)
	{
		super(NoxPlayer.class);
		this.plugin = plugin;
		this.config = conf;
	}
	
	@Override
	protected NoxPlayer craftNew(NoxPlayerAdapter adapter) {
		return adapter.getNoxPlayer();
	}
	
	@Override
	protected NoxPlayer craftNew(String name) {
		NoxPlayer player = new NoxPlayer(this, name);
		loadPlayer(player);
		return player;
	}
	
	@Override
	protected Map<String, NoxPlayer> craftNewStorage() {
		return new HashMap<String, NoxPlayer>();
	}

	public List<File> getAllDeprecatedPlayerFiles() {
		List<File> ret = new ArrayList<File>();
		try {
			for (File f : NoxCore.getInstance().getDataFile("playerdata").listFiles()) {
				String name = f.getName().replace(".yml", "");
				if (!name.matches("(\\w{8})-?(\\w{4})-?(\\w{4})-?(\\w{4})-?(\\w{12})"))
					ret.add(f);
			}
		} catch (NullPointerException e) {//We don't have a nullfix yet... 
			
		}
		return ret;
	}
	
	/**
	 * @deprecated Uses old file names... This will break.. Use " MISSING METHOD " instead. 
	 * @return list of player names.
	 */
	public List<String> getAllPlayerNames() {
		List<String> ret = new ArrayList<String>();
		if (isMultiFile())
		{
			try {
				for (File f : NoxCore.getInstance().getDataFile("playerdata").listFiles()) {
					String name = f.getName().replace(".yml", "");
					if (!name.matches("(\\w{8})-?(\\w{4})-?(\\w{4})-?(\\w{4})-?(\\w{12})"))
						ret.add(name);
				}
			} catch (NullPointerException e) {//We don't have a nullfix yet...
				 
			}
		} else {
			for (ConfigurationNode node : config.getNode("players").getNodes())
				ret.add(node.getName());
		}
		
		for (NoxPlayer p : getLoadedPlayers())
			if (!ret.contains(p.getName()))
				ret.add(p.getName());
		
		return ret;
	}
	
	public NoxPlayer[] getLoadedPlayers() {
		return getPlayerMap().values().toArray(new NoxPlayer[0]);
	}	
	
/**
	 * ARE YOU CRAZY!?
	 * @deprecated returns the param that was given. 
	 * @param player
	 * @return player param
	 */
	public NoxPlayer getPlayer(NoxPlayer player) {
		return player;
	}
	
	/**
	 * Gets the player file. <br><br>
	 * 
	 * Will not auto move files that are not updated for 1.8 UID.
	 *
	 * New files will grab UID version of file.
	 *
	 * @see #getPlayerFile(String)
	 * @param noxPlayer the NoxPlayer object
	 * @return the player file
	 */
	public File getPlayerFile(NoxPlayer noxPlayer) {
		File old = getPlayerFile(noxPlayer.getName() + ".yml");
		File supposed = getPlayerFile("NEED-UID", noxPlayer.getName() + ".yml");
		File uidF = getPlayerFile(noxPlayer.getUID() + ".yml");
		if (old.exists() && noxPlayer.getUUID() == null)
			if (FileUtil.copy(old, supposed))
				if (!old.delete())
					old.deleteOnExit(); //Attempt to delete on exit.
		if (noxPlayer.getUUID() == null)
			return supposed;
		else {
			if (supposed.exists()) {
				if (FileUtil.copy(supposed, uidF))
					if (!supposed.delete())
						supposed.deleteOnExit();
			} else if (old.exists()) {
				if (FileUtil.copy(old, uidF))
					if (!old.delete())
						old.deleteOnExit();
			}
			
			return uidF;
			
		}
	}
	
	/**
	 * Gets the player file.
	 *
	 * @param name of the player
	 * @return the player file
	 */
	public File getPlayerFile(String... path)
	{
		String[] oPath = path;
		path = new String[oPath.length+1];
		path[0] = "playerdata";
		
		//Rough Copy.
		for (int i = 0; i < oPath.length; i++)
			path[i+1] = oPath[i];
		
		return NoxCore.getInstance().getDataFile(path);
	}

	/**
	 * Gets the player node.
	 * <b>INTERNAL METHOD</b> Best not to use this!
	 * @param isUID 
	 * @param name the name
	 * @return the player node
	 */
	public ConfigurationNode getPlayerNode(NoxPlayer player)
	{
		if (isMultiFile() && !(player.getPersistantData() instanceof FileConfiguration))
		{
			FileConfiguration c = new FileConfiguration(getPlayerFile(player)); 
			ConfigurationNode old = player.getPersistantData();
			if (old != null)
				for (Entry<String, Object> entry : old.getValues().entrySet()) //Copy data.
					c.set(entry.getKey(), entry.getValue());
			
			return c;
		}
		else if (isMultiFile() && (player.getPersistantData() instanceof FileConfiguration))
			return (FileConfiguration) player.getPersistantData();
		else if (config != null && !isMultiFile())
			if (player.getUUID() == null && player.getName() != null)
				return config.getNode("players").getNode(player.getName());
			else if (player.getUUID() != null && player.getUUID() != UUIDUtil.ZERO_UUID)
				return config.getNode("players").getNode(player.getUUID().toString());
			else
				return null;
		else
			return null;
	}
	
	public NoxCore getPlugin() { return plugin; }
	
	//////// HELPER FUNCTIONS
	/**
	 * Checks if is multi file.
	 * <b>INTERNALLY USED METHOD</b>
	 * @return true, if is using the multi file structure for player data.
	 */
	public boolean isMultiFile() { return NoxCore.isUseUserFile(); }
	
	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#load()
	 */
	public void load() {
		Collection<String> pls = getPlayerMap().keySet();

		getPlayerMap().clear();
		config.load();
		
		for (Player player : Bukkit.getOnlinePlayers())
			loadOrCreate(player.getName());
		
		for (String name : pls)
			if (!isLoaded(name))
				loadOrCreate(name);
		
	}
	
	private void loadOrCreate(String name) {
		loadPlayer(name);
	}
	
	/**
	 * Load player.
	 *
	 * @param noxPlayer the NoxPlayer object to load
	 */
	public void loadPlayer(NoxPlayer noxPlayer) {
		ConfigurationNode persistant_data = getPlayerNode(noxPlayer); 

		if (persistant_data != noxPlayer.getPersistantData()) {//Remove desyncs...
			noxPlayer.setPersistantData(persistant_data);
			getPlugin().log(Level.INFO, "Player data object mismatch. Syncing objects for player\"" + noxPlayer.getName() +":"+ noxPlayer.getUID() + "\"");
		}
		
		if (persistant_data instanceof FileConfiguration)
		{
			FileConfiguration fNode = (FileConfiguration) persistant_data;
			fNode.load();
		} else {
			load();
		}
		
		super.loadPlayer(noxPlayer);
		
		for (IPlayerManager<?> manager : managers)
			if (manager != this)
				manager.loadPlayer(noxPlayer);
	}
	
	/**
	 * Load player.
	 *
	 * @param name of the player
	 */
	public void loadPlayer(String name) {
		loadPlayer(getPlayer(name));
	}

	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#save()
	 */
	public void save() {
		super.save();
		
		if (!isMultiFile())
			config.save();
	}

	/**
	 * Save player.
	 *
	 * @param player the player
	 */
	public void savePlayer(NoxPlayer player) 
	{
//		if (!lockFiles.isLocked()) {
//			getPlugin().log(Level.SEVERE, "Could not save player '" + player.getName() + ":=" + player.getUID() + "' due to file lock.");
//			return;
//		}
		
		ConfigurationNode persistant_data = getPlayerNode(player);
		if (persistant_data != player.getPersistantData()) {//Remove desyncs...
			player.setPersistantData(persistant_data); 
			getPlugin().log(Level.INFO, "Player data object mismatch. Syncing objects for player\"" + player.getName() +":"+ player.getUID() + "\"");
		}
		
		player.save();
		for (IPlayerManager<?> manager : managers) { //Iterate through all plugin.
			if (manager != this)
				manager.savePlayer(player); 
		}
		
		if (persistant_data instanceof FileConfiguration)
		{
			persistant_data.set("last.save", System.currentTimeMillis()); //Snapshot
			FileConfiguration configNode = (FileConfiguration) persistant_data;
			configNode.save();
		} else {
			persistant_data.set("last.save", (long) -1); //Magic Number
		}
	}

	/**
	 * Are you crazy!
	 * @deprecated returns the specified argument.. 
	 */
	protected NoxPlayer craftNew(NoxPlayer adapter) {
		return adapter;
	}

	public void complain(SafeLocker lock) {
		complain(lock, null);
	}

	public boolean isLocked() {
		return isLocked.get();
	}

	public LockerCaller getCaller() {
		return lockCaller;
	}

	public boolean tryLock(LockerCaller caller) {

		boolean ret = false;
		if (getCaller() == null) {
			ret = true;
			isLocked.set(true); 
		} else if (getCaller() == caller) {
			ret = true;
			isLocked.set(true);
		}
		
		if (ret)
			lockCaller = caller;
		return ret;
	}

	public boolean tryUnlock(LockerCaller caller) {
		boolean ret = false;
		if (getCaller() == null) {
			ret = isLocked.compareAndSet(true, false);
			if (!ret)
				getPlugin().log(Level.SEVERE, "PlayerManager has a lock from an unknown caller. When the lock was previously activated.");
		} else if (getCaller() == caller) {
			ret = true;
			isLocked.set(true);
			lockCaller = null;
		}
		return ret;
	}

	public void complain(SafeLocker lock, LockerCaller complainer) {
		getPlugin().log(Level.SEVERE, "PlayerManager is on file lock. " + ((complainer != null) ? ("Complainer: " + complainer.getClass().getName()):""));
	}
}
