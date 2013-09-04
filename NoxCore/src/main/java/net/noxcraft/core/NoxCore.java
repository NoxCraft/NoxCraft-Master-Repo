package net.noxcraft.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import java.util.logging.Level;

import net.noxcraft.core.permissions.NoxPermission;
import net.noxcraft.core.tp.BaseHome;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;

public class NoxCore extends PluginBase {
	private static NoxCore instance;
	
	private List<NoxPermission> permissions = new ArrayList<NoxPermission>();
	private transient WeakHashMap<String, NoxPermission> permission_cache = new WeakHashMap<String, NoxPermission>();
	
	@Override
	public boolean command(CommandSender sender, String command, String[] args) {
		return false;
	}
	
	@Override
	public void disable() {
		instance = null;
	}
	
	@Override
	public void enable() {
		instance = this;
		
		//Serializable Objects
		ConfigurationSerialization.registerClass(BaseHome.class);
		ConfigurationSerialization.registerClass(SafeLocation.class);
		
		//Not ready to enable...
		log(Level.SEVERE, "This plugin has no functionality. Why are we running this? Self Disabling...");
		setEnabled(false);
		
	}
	
	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}
	
	@Override
	public void permissions() {
		addPermission(new NoxPermission("core.*", "All noxcore permissions (Including admin nodes).", PermissionDefault.OP,
				new NoxPermission("core.reload", "Reload command for Nox Core", PermissionDefault.OP),
				new NoxPermission("core.save", "Save permission for saving everything in core.", PermissionDefault.OP),
				new NoxPermission("core.load", "Load permission for loading everything in core.", PermissionDefault.OP)
				));
	}
	
	public void addPermission(NoxPermission permission)
	{
		if (permission_cache.containsKey(permission.getName()))
			return;
		permission_cache.put(permission.getName(), permission);
		permissions.add(permission);
		
		if (permission.getChildren().length > 0)
			for (NoxPermission child : permission.getChildren())
				addPermission(child);
		
		if (permission.getParentNodes().length > 0)
			for (String node : permission.getParentNodes())
			{
				NoxPermission perm = new NoxPermission(node, "Parent node of " + permission.getName() + ".", PermissionDefault.OP);
				addPermission(perm);
			}
		
		loadPermission(permission);
	}
	
	public void removePermission(String name)
	{
		removePermission(name, false);
	}
	
	public void removePermission(String name, boolean force)
	{
		if (permission_cache.containsKey(name))
		{
			Bukkit.getPluginManager().removePermission(name);
			NoxPermission perm = permission_cache.get(name);
			permission_cache.remove(name);
			permissions.remove(perm);
		} 
		else if (force)
		{
			NoxPermission permFound = null;
			for (NoxPermission perm : permissions)
				if (perm.getName().equals(name))
				{
					Bukkit.getPluginManager().removePermission(name);
					permFound = perm;
					break;
				}
			if (permFound != null)
				permissions.remove(permFound);
			
		}
	}
	
	/**
	 * Aquires all permission nodes.
	 * 
	 * See addPermission and removePermission for manipulating this list.
	 * @return an unmodifiable list of permissions
	 */
	public final List<NoxPermission> getAllNoxPerms()
	{
		return Collections.unmodifiableList(permissions);
	}

	public static NoxCore getInstance() {
		return instance;
	}
}
