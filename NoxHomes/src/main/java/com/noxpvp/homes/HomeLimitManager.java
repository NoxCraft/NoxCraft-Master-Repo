package com.noxpvp.homes;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;

public class HomeLimitManager implements Persistant {
	
	private NoxHomes plugin;
	private FileConfiguration config;
	private static boolean cumulativeLimits = false;
	private static boolean superPerms = true;
	
	/**
	 * @return the superPerms
	 */
	public static final boolean isSuperPerms() {
		return superPerms;
	}

	/**
	 * @param superPerms the superPerms to set
	 */
	public static final void setSuperPerms(boolean superPerms) {
		HomeLimitManager.superPerms = superPerms;
	}

	public HomeLimitManager()
	{
		this(NoxHomes.getInstance());
	}
	
	public HomeLimitManager(NoxHomes plugin)
	{
		this.plugin = plugin;
		config = new FileConfiguration(plugin.getDataFile("limits.yml"));
	}
	
	public boolean inRange(OfflinePlayer player, int count)
	{
		return inRange(player.getName(), count);
	}
	
	public boolean inRange(NoxPlayerAdapter noxplayer, int count)
	{
		int limit = 0;
		
		NoxPlayer p = getNoxPlayer(noxplayer);
		
		limit = getLimit(p);
		
		if (limit < 0)
			return true;
					
		return count <= limit;
	}
	
	public boolean inRange(String playerName, int count)
	{
		return inRange(getNoxPlayer(playerName), count);
	}
	
	public int getLimit(String playerName)
	{
		return getLimit(getNoxPlayer(playerName));
	}
	
	public int getLimit(NoxPlayerAdapter p)
	{
		NoxPlayer nPlayer = getNoxPlayer(p);
		String player = nPlayer.getName();
		
		World world = nPlayer.getLastWorld();
		
		int limit = 0;
		
		if (!config.getNode("player").getKeys().contains(player)) {
			for (String node : config.getNode("group").getKeys())
				if (superPerms || !VaultAdapter.permission.hasGroupSupport()) 
				{
					if (VaultAdapter.permission.has(world, player, "group."+node))
						if (cumulativeLimits)
							limit += config.get("group."+node, int.class);
						else
							limit = (limit <= config.get("group."+node, int.class)?config.get("group."+node, int.class):limit);
				} else if (VaultAdapter.permission.playerInGroup(world, player, node)) {
					if (cumulativeLimits)
						limit += config.get("group."+node, int.class);
					else
						limit = (limit <= config.get("group."+node, int.class)?config.get("group."+node, int.class):limit);
				}
		} else 
			limit = config.get("player."+ player, 0);
		
		
		return limit;
	}
	
	public boolean canAddHome(String playerName)
	{
		return canAddHome(getNoxPlayer(playerName));
	}
	
	public boolean canAddHome(NoxPlayer player)
	{
		HomesPlayer p = new HomesPlayer(player);
		
		int count = p.getHomeCount(); //Could possibly single lined.. Unsure at moment.
		
		return inRange(p, ++count);
	}
	
	public static boolean usingCumulativeLimits() { return cumulativeLimits; }
	
	public static void setCumulativeLimits(boolean use) { cumulativeLimits = use; }
	
	public void save() {
		config.set("cumulativeLimits", usingCumulativeLimits());
		config.set("superPerms", isSuperPerms());
		config.save();
	}

	public void load() {
		if (!config.exists())
		{
			genDefault();
		}
		
		
		config.load();
		setSuperPerms(config.get("superPerms", isSuperPerms()));
		setCumulativeLimits(config.get("cumulativeLimits", cumulativeLimits));
	}
	
	private void genDefault() {
		config.setHeader("This file is generated to give you an idea for syntax of setting limits.");
		config.addHeader("There are two root nodes. \"group\" and \"player\"");
		config.addHeader("Use those nodes to add limits. Below is default example.");
		config.addHeader("");
		config.addHeader("Values below 0 are infinite homes!");
		ConfigurationNode node = config.getNode("group");
		node.set("peasant", 1);
		node.set("vip", 2);
		node.set("sponsor", 3);
		node.set("king", 4);
		node.set("imperial", 5);
		
		node = config.getNode("player");
		node.set("coaster3000", -1);
		
		save();
	}

	private static NoxPlayer getNoxPlayer(NoxPlayerAdapter adapt)
	{
		if (adapt instanceof NoxPlayer)
			return (NoxPlayer) adapt;
		else
			return adapt.getNoxPlayer();
	}
	
	private static NoxPlayer getNoxPlayer(String name)
	{
		return NoxCore.getInstance().getPlayerManager().getPlayer(name);
	}
}
