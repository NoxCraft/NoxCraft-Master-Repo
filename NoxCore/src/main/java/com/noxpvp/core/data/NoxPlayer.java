package com.noxpvp.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.internal.PermissionHandler;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.VaultAdapter;

public class NoxPlayer implements Persistant, NoxPlayerAdapter {
	private final String name;
	private ConfigurationNode temp_data = new ConfigurationNode();
	private ConfigurationNode persistant_data = null;
	private PlayerManager manager;
	
	private WeakHashMap<String, CoolDown> cd_cache;
	private List<CoolDown> cds;
	
	public NoxPlayer(NoxPlayer player)
	{
		cds = new ArrayList<CoolDown>();
		cd_cache = new WeakHashMap<String, CoolDown>();
		this.name = player.name;
		this.temp_data = player.temp_data;
		this.persistant_data = player.persistant_data;
		this.manager = player.manager;
		load();
	}
	
	public NoxPlayer(PlayerManager mn, String name) {
		cds = new ArrayList<CoolDown>();
		cd_cache = new WeakHashMap<String, CoolDown>();
		manager = mn;
		this.name = name;
		load();
	}
	
	private PermissionHandler permHandler;
	
	public String getName(){
		return name;
	}
	
	public Location getLastDeathLocation()
	{
		SafeLocation l = null;
		return ((l=this.persistant_data.get("last.death.location", SafeLocation.class))==null?null:l.toLocation()); //Nice handy work here!
	}
	
	public long getLastDeathTS()
	{
		return (this.persistant_data.get("last.death.timestamp", (long)0));
	}
	
	public void setLastDeathLocation(Location loc)
	{
		this.persistant_data.set("last.death.location", new SafeLocation(loc));
	}
	
	public void setLastDeathTS(long stamp)
	{
		this.persistant_data.set("last.death.timestamp", stamp);
	}
	
	public void setLastDeathTS()
	{
		setLastDeathTS((NoxCore.isUsingNanoTime()?System.nanoTime(): System.currentTimeMillis()));
	}
	
	public World getLastWorld() {
		World w = null;
		if (getPlayer() != null)
			w = getPlayer().getWorld();
		else
			w = Bukkit.getWorld(persistant_data.get("last.world", String.class, "NONE"));
		return w;
	}
	
	public String getLastWorldName() {
		World w = getLastWorld();
		if (w != null)
			return w.getName();
		return persistant_data.get("last.world", String.class, "NONE");
	}
	
	public NoxPlayer getNoxPlayer() { return this; }
	
	public final ConfigurationNode getPersistantData() { return persistant_data;}
	public final ConfigurationNode getTempData() { return temp_data; }
	
	public final String getPlayerName(){ return name;}
	
	public OfflinePlayer getOfflinePlayer() { return Bukkit.getOfflinePlayer(getPlayerName());}
	
	public Player getPlayer() { return Bukkit.getPlayerExact(getPlayerName()); }
	
	public Double getMoney() { return VaultAdapter.economy.getBalance(getPlayerName(), getLastWorldName()); }
	public Double getMoney(String worldName) { return VaultAdapter.economy.getBalance(getPlayerName(), worldName); }
	
	public long getFirstJoin() {
		return getFirstJoin(true);
	}
	
	public long getFirstJoin(boolean cached)
	{
		if (cached)
			return persistant_data.get("first.join", long.class);
		else
			return getOfflinePlayer().getFirstPlayed();
	}
	
	public long getLastJoin() {
		return getLastJoin(true);
	}
	
	public long getLastJoin(boolean cached)
	{
		if (cached)
			return persistant_data.get("last.join", long.class);
		else
			return getOfflinePlayer().getLastPlayed();
	}
	
	public SafeLocation getLastLocation() {
		if (getPlayer() != null)
			return persistant_data.get("last.location", new SafeLocation(getPlayer().getLocation()));
		else
			return persistant_data.get("last.location", SafeLocation.class);
	}
	
	public void saveLastLocation(){
		if (getPlayer() != null)
			persistant_data.set("last.location", new SafeLocation(getPlayer().getLocation()));
	}
	
	public void saveLastLocation(Player player){
		if (!player.getName().equals(name))
			throw new IllegalArgumentException("Must be the same player as object holder");
		
		persistant_data.set("last.location", new SafeLocation(player.getLocation()));
	}
	
	public int getVotes()
	{
		return persistant_data.get("vote-count", (int)0);
	}
	
	public void setVotes(int amount)
	{
		persistant_data.set("vote-count", amount);
	}
	
	public void incrementVote() {
		setVotes(getVotes() + 1);
	}
	
	public void decrementVote() {
		setVotes(getVotes() - 1);
	}
	
	public boolean hasPermission(String permNode)
	{
		if (getPlayer() != null)
			return permHandler.hasPermission(getPlayer(), permNode);
		else if (VaultAdapter.isPermissionsLoaded())
			return VaultAdapter.permission.has(getLastWorld(), getPlayerName(), permNode);
		else
			return false;
	}
	
	public boolean hasPermissions(String... permissions)
	{
		for (String perm : permissions)
			if (!hasPermission(perm))
				return false;
		return true;
	}

	public void save() {
		persistant_data.set("cooldowns", getCoolDowns());
		if (persistant_data instanceof FileConfiguration)
		{
			FileConfiguration configNode = (FileConfiguration) persistant_data;
			configNode.save();
		}
	}

	public void load() {
		if (persistant_data == null)
			persistant_data = manager.getPlayerNode(name);
		
		if (persistant_data instanceof FileConfiguration)
		{
			FileConfiguration fNode = (FileConfiguration) persistant_data;
			fNode.load();
		}
		
		cds = persistant_data.getList("cooldowns", CoolDown.class);
		
		rebuild_cache();
	}
	
	public void rebuild_cache() {
		cd_cache.clear();
		for (CoolDown cd: cds)
			cd_cache.put(cd.getName(), cd);
	}

	public List<CoolDown> getCoolDowns() {
		return cds;
	}
	
	public boolean isCooldownActive(String name)
	{
		if (cd_cache.containsKey(name))
			return cd_cache.get(name).expired();
		else
			return false;
	}
	
	public boolean isCooldownExpired(String name)
	{
		if (cd_cache.containsKey(name))
			return cd_cache.get(name).expired();
		else
			return true;
	}
	
	public void removeCooldDown(String name)
	{
		if (cd_cache.containsKey(name))
		{
			cds.remove(cd_cache.get(name));
			cd_cache.remove(name);
		}
	}
	
	public boolean addCoolDown(String name, long length)
	{
		if (cd_cache.containsKey(name) && !cd_cache.get(name).expired())
			return false;
		CoolDown cd;
		
		long time = 0;
		if (NoxCore.isUsingNanoTime())
			time = System.nanoTime() + length;
		
		cd = new CoolDown(name, time, NoxCore.isUsingNanoTime());
		
		cds.add(cd);
		cd_cache.put(cd.getName(), cd);
		return true;
	}
}
