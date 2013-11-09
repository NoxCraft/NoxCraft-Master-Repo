package com.noxpvp.homes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.homes.tp.BaseHome;

public class HomeManager implements Persistant { //FIXME: Javadocs
	
	private Map<String, HomesPlayer> players;
	
	protected NoxCore plugin;
	
	public HomeManager(NoxCore core)
	{
		this.plugin = core;
		players = new HashMap<String, HomesPlayer>();
	}
	
	public List<BaseHome> getHomes(OfflinePlayer player)
	{
		return getHomes(player.getName());
	}
	
	public List<BaseHome> getHomes(String player)
	{
		return getPlayer(player).getHomes();
	}
	
	public boolean isLoaded(OfflinePlayer player)
	{
		return isLoaded(player.getName());
	}
	
	public boolean isLoaded(String player)
	{
		return (players.containsKey(player));
	}
	
	public boolean hasData(OfflinePlayer player)
	{
		return hasData(player.getName());
	}
	
	public boolean hasData(String player)
	{
		return getPlayer(player).hasHomes();
	}
	
	public HomesPlayer getPlayer(OfflinePlayer player)
	{
		return getPlayer(player.getName());
	}
	
	public HomesPlayer getPlayer(String player) {
		if (players.containsKey(player))
			return players.get(player);
		else
		{
			players.put(player, new HomesPlayer(getNoxPlayer(player)));
			return players.get(player);
		}
	}

	public void load()
	{
		players.clear();
		for (Player player : Bukkit.getOnlinePlayers())
			getPlayer(player);
	}
	
	public void loadHomes(OfflinePlayer player)
	{
		loadHomes(player.getName());
	}
	
	public void loadHomes(String player)
	{
		getPlayer(player);
	}
	
	public void save()
	{
		for (HomesPlayer player : players.values())
			player.save();
	}
	
	public void unload(OfflinePlayer player)
	{
		unload(player.getName());
	}
	
	public void unload(String name)
	{
		if (players.containsKey(name))
			players.remove(name);
	}
	
	public void unloadAndSave(OfflinePlayer player)
	{
		unloadAndSave(player.getName());
	}
	
	public void unloadAndSave(String name)
	{
		getPlayer(name).save();
		unload(name);
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
		HomesPlayer player = getPlayer(owner);
		return player.getHome(name);
	}
	/**
	 * Erases all home data on every player.
	 * 
	 */
	public void clear() {
		
		
		PlayerManager pm = plugin.getPlayerManager();
		List<String> names = pm.getAllPlayerNames();
		
		for (HomesPlayer player : players.values())
		{
			if (player.getNoxPlayer(false) == null)
			{
				names.add(player.getPlayerName());
				continue;
			}
			player.setHomes(null);
		}
		
		names.removeAll(new ArrayList<String>(players.keySet()));
		for(String name : names)
		{
			boolean notMem = !pm.isPlayerInMemory(name);
			pm.getPlayer(name).getPersistantData().remove("homes");
			
			if (notMem)
				pm.unloadAndSavePlayer(name);
		}
	}
	
	/**
	 * Gets the nox player.<br />
	 * <b>Internal Method Only</b>
	 * 
	 * @param player
	 *            the player
	 * @return the nox player
	 */
	private static NoxPlayer getNoxPlayer(String player) { return NoxCore.getInstance().getPlayerManager().getPlayer(player);} 
}
