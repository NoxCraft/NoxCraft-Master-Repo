package com.noxpvp.homes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;

import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;

@SuppressWarnings("unchecked")
public class HomesPlayer extends BaseNoxPlayerAdapter implements Persistant {
	
	private List<BaseHome> homes = new ArrayList<BaseHome>();;
	private Map<String, Integer> homes_index = new HashMap<String, Integer>();;
	
	/**
	 * Instantiates a new homes playerRef.
	 *
	 * @param playerName the name of the playerRef.
	 */
	public HomesPlayer(String playerName)
	{
		super(playerName);
	}
	
	/**
	 * Instantiates a new homes playerRef.
	 *
	 * @param playerRef an OfflinePlayer
	 */
	public HomesPlayer(OfflinePlayer player)
	{
		super(player);
	}
	
	public HomesPlayer(NoxPlayerAdapter player)
	{
		super(player);
	}
	
	/**
	 * Gets the list of homes.
	 *
	 * @return homes List
	 */
	public List<BaseHome> getHomes() {
		return Collections.unmodifiableList(homes);
	}
	
	/**
	 * Gets the home names.
	 *
	 * @return the home names
	 */
	public List<String> getHomeNames()
	{
		return Conversion.convert(homes_index.keySet(), List.class);
	}
	
	/**
	 * Checks for homes.
	 *
	 * @return true, if successful
	 */
	public boolean hasHomes() {
		return homes.size() > 0;
	}

	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#save()
	 */
	public void save() {
		getNoxPlayer().getPersistantData().set("homes", getHomes());
	}

	/* (non-Javadoc)
	 * @see com.noxpvp.core.Persistant#load()
	 */
	public void load() {
		setHomes(getNoxPlayer().getPersistantData().getList("homes", BaseHome.class));
	}
	
	/**
	 * Adds the home to data.
	 * <br/>
	 * Saves data after completion.
	 * @param home of type BaseHome to add.
	 */
	public void addHome(BaseHome home)
	{
		if (homes_index.containsKey(home.getName()))
			homes.set(homes_index.get(home.getName()), home);
		else {
			homes_index.put(home.getName(), homes_index.size());
			homes.add(home);
		}
		
		save();
	}
	
	/**
	 * Removes the home from data.
	 * <br/>
	 * Saves data after completion.
	 *
	 * @param home to remove.
	 * @return true if successful.
	 */
	public boolean removeHome(BaseHome home)
	{
		if (homes_index.containsKey(home.getName()))
		{
			if (homes.contains(home))
			{
				homes_index.remove(home.getName());
				homes.remove(home);
			}
			else 
			{
				int index = homes_index.get(home.getName());
				homes.remove(index);
				homes_index.remove(home.getName());
			}
		}
		else
			return false;
		save();
		return true;
	}

	/**
	 * Sets the homes.
	 * <br/>
	 * Saves data after completion.
	 * @param list the replacement list of homes.
	 */
	protected final void setHomes(List<BaseHome> list) {
		homes.clear();
		homes_index.clear();
		
		int i = 0;
		
		if (list != null)
			for (BaseHome home : list)
			{
				homes.add(home);
				homes_index.put(home.getName(), i);
			}
		
		save();
	}

	/**
	 * Gets the specified home.
	 *
	 * @param name of the home
	 * @return the home
	 */
	public BaseHome getHome(String name) {
		if (name == null)
			if (homes_index.containsKey(DefaultHome.DEF_NAME))
				return homes.get(homes_index.get(DefaultHome.DEF_NAME));
			else
				return null;
		else if (homes_index.containsKey(name))
			return homes.get(homes_index.get(name));
		else
			return null;
	}
	
}