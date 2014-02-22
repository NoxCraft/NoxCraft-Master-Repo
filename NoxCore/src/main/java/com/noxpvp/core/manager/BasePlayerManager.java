package com.noxpvp.core.manager;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.data.NoxPlayerAdapter;

public abstract class BasePlayerManager<T extends NoxPlayerAdapter> implements IPlayerManager<T> {
	private PlayerManager pm = null;
	
	private Map<String, T> players;

	private Class<T> typeClass;
	
	/**
	 * This should never be called in the constructor.
	 * @return PlayerManager from NoxCore
	 */
	protected PlayerManager getCorePlayerManager() {
		if (pm == null)
			pm = PlayerManager.getInstance();
		return pm;
	}
	
	public BasePlayerManager(Class<T> t) {
		this.typeClass = t;
	}

	public T[] getLoadedPlayers() {
		return LogicUtil.toArray(players.values(), typeClass);
	}

	public final T getPlayer(OfflinePlayer player) {
		if (player == null)
			return null;
		return getPlayer(player.getName());
	}

	public final T getPlayer(String name) {
		T player = null;
		if (players.containsKey(name))
			player = players.get(name);
		else {
			player = craftNew(name);
			players.put(name, player);
		}
		return player;
	}

	/**
	 * Required to grab objects.
	 * @param name of player.
	 * @return Object.
	 */
	protected abstract T craftNew(String name);
	
	/**
	 * Required to store player data.
	 * @return
	 */
	protected abstract Map<String, T> craftNewStorage();

	public void savePlayer(T player) {
		player.save();
	}

	public final void savePlayer(String name) {
		if (isLoaded(name))
			savePlayer(getPlayer(name));
	}

	public final void savePlayer(OfflinePlayer player) {
		savePlayer(player.getName());
	}

	public final boolean isLoaded(String name) {
		return players.containsKey(name);
	}

	public final boolean isLoaded(OfflinePlayer player) {
		return isLoaded(player.getName());
	}

	/**
	 * Unload and save player.
	 * 
	 * @see #unloadPlayer(String)
	 * @see #savePlayer(String)
	 * @param name the name
	 */
	public final void unloadAndSavePlayer(String name) {
		savePlayer(name);
		unloadPlayer(name);
	}
	
	/**
	 * Unload and save player.
	 * @see #unloadAndSavePlayer(String)
	 * @see #unloadPlayer(String)
	 * @see #savePlayer(String)
	 * @param name the name
	 */
	public final void unloadAndSavePlayer(OfflinePlayer player) {
		unloadAndSavePlayer(player.getName());
	}
	
	/**
	 * Unload if player is offline.
	 * <br/>
	 * <b> WARNING IF THERE ARE PLUGINS THAT ARE IMPROPERLY CACHING THIS OBJECT. IT WILL NEVER TRUELY UNLOAD</b>
	 * @param name of the player
	 * @return true, if it unloads the player from memory.
	 */
	public boolean unloadIfOffline(String name) {
		if (isLoaded(name) && (getPlayer(name).getPlayer() == null || !getPlayer(name).getPlayer().isOnline()))
		{
			unloadAndSavePlayer(name);
			return true;
		}
		return false;
	}
	
	protected abstract boolean preUnloadPlayer(String name);
	
	public void unloadPlayer(String name) {
		if (preUnloadPlayer(name))
			if (isLoaded(name))
				players.remove(name);
	}

	public final void unloadPlayer(OfflinePlayer player) {
		unloadPlayer(player.getName());
	}

	public void loadPlayer(T player) {
		player.load();
	}

	public void loadPlayer(String name) {
		loadPlayer(getPlayer(name));
	}

	public void loadPlayer(OfflinePlayer player) {
		loadPlayer(getPlayer(player));
	}
	
	public void save() {
		for (T p : getLoadedPlayers())
			savePlayer(p);
	}
	
	public void load() {
		for (Player p : Bukkit.getOnlinePlayers())
			loadPlayer(p);
	}
}
