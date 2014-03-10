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
	
	private Map<String, CoreBar> coreBars = new HashMap<String, CoreBar>();
	private Map<String, CoreBoard> coreBoards = new HashMap<String, CoreBoard>();
	private Map<String, CoreBox> coreBoxes = new HashMap<String, CoreBox>();
	
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
	
	/**
	 * 
	 * @param name The Key
	 * @param bar The CoreBar to add
	 * @throws NullPointerException If the key or CoreBar are null
	 */
	public void addCoreBar(CoreBar bar){
		if (bar == null)
			throw new IllegalArgumentException("Cannot use null CoreBar to add to active CoreBar list");
		
		this.coreBars.put(bar.p.getName(), bar);
	}

	/**
	 * 
	 * @param name The Key
	 * @param board The CoreBoard to add
	 * @throws NullPointerException If the key or CoreBoard are null
	 */
	public void addCoreBoard(CoreBoard board){
		if (board == null)
			throw new IllegalArgumentException("Cannot use null CoreBoard to add to active CoreBoard list");
		
		this.coreBoards.put(board.p.getName(), board);
	}
	
	public void addCoreBox(CoreBox box) {
		if (box == null)
			throw new IllegalArgumentException("Cannot use null CoreBox to add to active CoreBox list");
		
		if (box.isValid()){
			this.coreBoxes.put(box.getPlayer().getName(), box);
			box.register();
		}
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
	 * @return CoreBox The CoreBox
	 * @throws NullPointerException If the key is null
	 */
	public CoreBox getCoreBox(String name){
		if (name == null)
			throw new NullPointerException("Cannot use with null key");
		
		return this.coreBoxes.get(name);
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
	 * Gets all the currently active CoreBoxes
	 * 
	 * @return Collection<BaseCoreBox> The CoreBoxs
	 */
	public Collection<CoreBox> getCoreBoxes(){
		return this.coreBoxes.values();
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
	 * @return boolean If there is a CoreBoard active with the specific key
	 * @throws NullPointerException If the key is null
	 */
	public boolean hasCoreBoard(String name){
		if (name == null)
			throw new NullPointerException("Cannot check with null key");
			
		return this.coreBoards.containsKey(name);
	}
	
	

	
	
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
	
	/**
	 * 
	 * @param name The key for the CoreBar to remove
	 * @return PlayerManager This instance
	 * @throws NullPointerException If the key is null
	 */
	public void removeCoreBar(String name){
		if (name == null)
			throw new NullPointerException("Cannot remove null Key from list");
		this.coreBars.remove(name); //FIXME: DESTROY INSTANCE SHIT. TIMERS AND MORE...
	}
	
	/** 
	 * @param name The key for the CoreBoard to remove
	 * @return PlayerManager This instance
	 * @throws NullPointerException If the key is null
	 */
	public void removeCoreBoard(String name){
		if (name == null)
			throw new NullPointerException("Cannot remove null Key from list");
		
		this.coreBoards.remove(name);
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
