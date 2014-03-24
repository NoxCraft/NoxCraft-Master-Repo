package com.noxpvp.homes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.manager.BasePlayerManager;
import com.noxpvp.homes.tp.BaseHome;

public class PlayerManager extends BasePlayerManager<HomesPlayer> { //FIXME: Javadocs
	private static PlayerManager instance;
	
	public static PlayerManager getInstance() {
		if (instance == null)
			instance = new PlayerManager();
		
		return instance;
	}
	
	private PlayerManager()
	{
		super(HomesPlayer.class);
	}
	
	public List<BaseHome> getHomes(OfflinePlayer player)
	{
		return getHomes(player.getName());
	}
	
	public List<BaseHome> getHomes(String player)
	{
		return getPlayer(player).getHomes();
	}
	
	public boolean hasData(OfflinePlayer player)
	{
		return hasData(player.getName());
	}
	
	public boolean hasData(String player)
	{
		return getPlayer(player).hasHomes();
	}
	
	public HomesPlayer getPlayer(NoxPlayerAdapter adapter)
	{
		return getPlayer(adapter.getPlayerName());
	}
	
	public void load()
	{
		getPlayerMap().clear();
		for (Player player : Bukkit.getOnlinePlayers())
			getPlayer(player);
	}
	
	public void save()
	{
		for (HomesPlayer player : getPlayerMap().values())
			player.save();
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
		getPlayer(owner).addHome(home);
	}
	
	public boolean removeHome(BaseHome home)
	{
		String owner = home.getOwner();
		return getPlayer(owner).removeHome(home);
	}
	/**
	 * Retrieves the named home of the named owner
	 * 
	 * @param owner
	 *            name
	 * 
	 * @param name
	 * 			  The name of the home or null for default home
	 * 
	 * @return home of type BaseHome or null if none exist with that name.
	 */
	public BaseHome getHome(String owner, String name)
	{
		return getHome(getPlayer(owner), name);
	}
	
	public BaseHome getHome(HomesPlayer player, String homeName) {
		return player.getHome(homeName);
	}
	
	/**
	 * Erases all home data on every player.
	 * 
	 */
	public void clear() {
		
		
		com.noxpvp.core.manager.PlayerManager pm = com.noxpvp.core.manager.PlayerManager.getInstance();
		List<String> names = pm.getAllPlayerNames();
		
		for (HomesPlayer player : getPlayerMap().values())
		{
			if (player.getNoxPlayer(false) == null)
			{
				names.add(player.getPlayerName());
				continue;
			}
			player.setHomes(null);
		}
		
		names.removeAll(new ArrayList<String>(getPlayerMap().keySet()));
		for(String name : names)
		{
			boolean notMem = !pm.isLoaded(name);
			pm.getPlayer(name).getPersistantData().remove("homes");
			
			if (notMem)
				pm.unloadAndSavePlayer(name);
		}
	}
	
	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}

	@Override
	protected HomesPlayer craftNew(String name) {
		return new HomesPlayer(name);
	}

	@Override
	protected Map<String, HomesPlayer> craftNewStorage() {
		return new HashMap<String, HomesPlayer>();
	}

	@Override
	protected boolean preUnloadPlayer(String name) {
		return true;
	}

}