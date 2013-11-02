package com.noxpvp.homes;

import org.bukkit.OfflinePlayer;

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
		plugin = NoxHomes.getInstance();
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
		
		String world = p.getLastWorldName();
		String player = p.getName();
		
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
		
		if (limit < 0)
			return true;
					
		return count <= limit;
	}
	
	public boolean inRange(String playerName, int count)
	{
		return inRange(getNoxPlayer(playerName), count);
	}
	
	public static boolean usingCumulativeLimits() { return cumulativeLimits; }
	
	public static void setCumulativeLimits(boolean use) { cumulativeLimits = use; }
	
	public void save() {
		config.set("cumulativeLimits", usingCumulativeLimits());
		config.set("superPerms", isSuperPerms());
		config.save();
	}

	public void load() {
		config.load();
		setSuperPerms(config.get("superPerms", isSuperPerms()));
		setCumulativeLimits(config.get("cumulativeLimits", cumulativeLimits));
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