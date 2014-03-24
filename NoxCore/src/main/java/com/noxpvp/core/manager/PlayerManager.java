package com.noxpvp.core.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.events.PlayerDataUnloadEvent;

public class PlayerManager extends BasePlayerManager<NoxPlayer> implements Persistant {

	private static PlayerManager instance;
	
	protected FileConfiguration config;
	
	private NoxCore plugin;
	
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

	public List<String> getAllPlayerNames() {
		List<String> ret = new ArrayList<String>();
		if (isMultiFile())
		{
			try {
				for (File f : NoxCore.getInstance().getDataFile("playerdata").listFiles())
					ret.add(f.getName().replace(".yml", ""));
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
		File uidF = getPlayerFile(noxPlayer.getUUID().toString() + ".yml");
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
		if (isMultiFile())
			return new FileConfiguration(getPlayerFile(player));
		else if (config != null)
			if (player.getUUID() == null && player.getName() != null)
				return config.getNode("players").getNode(player.getName());
			else if (player.getUUID() != null)
				return config.getNode("players").getNode(player.getUUID().toString());
			else
				return null;
		else
			return null;
	}
	
	private File getPlayerFile(boolean isUID, String name) {
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
		loadPlayer(getPlayer(name));
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
		loadPlayer(getPlayer(name));
	}
	
	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#save()
	 */
	public void save() {
		config.save();
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
	
	@Override
	protected NoxPlayer craftNew(String name) {
		return new NoxPlayer(this, name);
	}
	
	@Override
	protected Map<String, NoxPlayer> craftNewStorage() {
		return new HashMap<String, NoxPlayer>();
	}
	
	protected boolean preUnloadPlayer(String name) {
		/*PlayerDataUnloadEvent e = */CommonUtil.callEvent(new PlayerDataUnloadEvent(getPlayer(name), false));
		return true;
	}

}
