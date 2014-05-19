package com.noxpvp.core.manager;

import org.bukkit.OfflinePlayer;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;

public interface IPlayerManager<T extends NoxPlayerAdapter> extends Persistant {
	public T[] getLoadedPlayers();
	
	/**
	 * Gets the player. <br><br> Best implementation is to get players name and then use {@link #getPlayer(String)}
	 *
	 * @param player the player
	 * @return the player
	 */
	public T getPlayer(OfflinePlayer player);
	
	/**
	 * Gets the player with the specified name.
	 *
	 * @param name of player
	 * @return the player
	 */
	public T getPlayer(String name);
	
	public T getPlayer(NoxPlayer player);
	
	public NoxPlugin getPlugin();
	
	public void savePlayer(NoxPlayer player);
	public void savePlayer(T player);
	public void savePlayer(String name);
	public void savePlayer(OfflinePlayer player);
	
	public boolean isLoaded(String name);
	public boolean isLoaded(OfflinePlayer player);
	
	public void unloadPlayer(String name);
	public void unloadPlayer(OfflinePlayer player);
	
	public void loadPlayer(NoxPlayer player);
	public void loadPlayer(T player);
	public void loadPlayer(String name);
	public void loadPlayer(OfflinePlayer player);
}
