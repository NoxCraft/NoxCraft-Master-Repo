package com.noxpvp.core.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.events.PlayerDataUnloadEvent;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.gui.CoreBox;

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
	 * Gets the player file.
	 *
	 * @see #getPlayerFile(String)
	 * @param noxPlayer the NoxPlayer object
	 * @return the player file
	 */
	public File getPlayerFile(NoxPlayer noxPlayer) {
		return getPlayerFile(noxPlayer.getName());
	}
	
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
