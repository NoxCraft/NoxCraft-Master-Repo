package net.noxcraft.noxcore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import java.util.logging.Level;

import net.noxcraft.noxcore.permissions.NoxcraftPermission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;

public class NoxCore extends PluginBase {
	private List<NoxcraftPermission> permissions = new ArrayList<NoxcraftPermission>();
	private WeakHashMap<String, NoxcraftPermission> permission_cache = new WeakHashMap<String, NoxcraftPermission>();
	
	@Override
	public boolean command(CommandSender sender, String command, String[] args) {
		return false;
	}
	
	@Override
	public void disable() {
		
	}
	
	@Override
	public void enable() {
		log(Level.SEVERE, "This plugin has no functionality. Why are we running this? Self Disabling...");
		setEnabled(false);
	}
	
	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}
	
	@Override
	public void permissions() {
		addPermission(new NoxcraftPermission("core.*", "All noxcore permissions (Including admin nodes).", PermissionDefault.OP,
				new NoxcraftPermission("core.reload", "Reload command for Nox Core", PermissionDefault.OP),
				new NoxcraftPermission("core.save", "Save permission for saving everything in core.", PermissionDefault.OP),
				new NoxcraftPermission("core.load", "Load permission for loading everything in core.", PermissionDefault.OP)
				));
		addPermission(new NoxcraftPermission("homes.*", "All homes permissions including admin nodes!", PermissionDefault.OP ,
				new NoxcraftPermission("homes.admin.*", "All admin nodes." , PermissionDefault.OP,
						new NoxcraftPermission("homes.admin.load", "Load file permission node (Includes reloads)", PermissionDefault.OP),
						new NoxcraftPermission("homes.admin.save", "Save to file permission node", PermissionDefault.OP),
						new NoxcraftPermission("homes.admin.wipe", "Wipe all homes!", PermissionDefault.OP),
						new NoxcraftPermission("homes.admin.config", "Edit config options ingame.", PermissionDefault.OP)
				
						),
				new NoxcraftPermission("homes.history.*", "History nodes. Useful for mods. DOES contains admin nodes.", PermissionDefault.OP,
						new NoxcraftPermission("homes.history.read", "Permission to read home history.", PermissionDefault.OP),
						new NoxcraftPermission("homes.history.clear", "Permission to clear history.", PermissionDefault.OP)),
				new NoxcraftPermission("homes.sethome.*", "Permission to all sethome nodes.", PermissionDefault.OP,
						new NoxcraftPermission("homes.sethome.named", "Set named homes.", PermissionDefault.OP),
						new NoxcraftPermission("homes.sethome.*.anywhere", "Set homes anywhere.", PermissionDefault.OP),
						new NoxcraftPermission("homes.sethome.anywhere", "Set homes anywhere.", PermissionDefault.OP)),
				new NoxcraftPermission("homes.homelimit.unlimited", "Have unlimited homes.", PermissionDefault.OP),
				new NoxcraftPermission("homes.sethome.other.*", "Permission to all sethome nodes.", PermissionDefault.OP,
						new NoxcraftPermission("homes.sethome.other.named", "Set named homes.", PermissionDefault.OP),
						new NoxcraftPermission("homes.sethome.other.*.anywhere", "Set homes anywhere.", PermissionDefault.OP),
						new NoxcraftPermission("homes.sethome.other.anywhere", "Set homes anywhere.", PermissionDefault.OP)),
				new NoxcraftPermission("homes.delhome.*", "Permission to delete any of your own homes.", PermissionDefault.OP, 
						new NoxcraftPermission("homes.delhome.named", "Permission to delete a named home", PermissionDefault.OP),
						new NoxcraftPermission("homes.delhome", "Permission delete your own default home.", PermissionDefault.OP)),
				new NoxcraftPermission("homes.delhome.other.*", "Permission to delete anyones homes of any type.", PermissionDefault.OP,
						new NoxcraftPermission("homes.delhome.other.named", "Permission to deleted named homes by others", PermissionDefault.OP),
						new NoxcraftPermission("homes.delhome.other", "Permission to delete default homes of others.", PermissionDefault.OP)),
				new NoxcraftPermission("homes.getloc.*", "Permission to get locations of your homes.", PermissionDefault.OP, 
						new NoxcraftPermission("homes.getloc.named", "Get co ords to named home.", PermissionDefault.OP),
						new NoxcraftPermission("homes.getloc", "Get default co ords to home.", PermissionDefault.OP)),
				new NoxcraftPermission("homes.getloc.other.*", "Get position of others homes.", PermissionDefault.OP,
						new NoxcraftPermission("homes.getloc.other.named", "Get others locations of named homes", PermissionDefault.OP),
						new NoxcraftPermission("homes.getloc.other", "Get default home location of others.", PermissionDefault.OP))
				));
	}
	
	public void addPermission(NoxcraftPermission permission)
	{
		if (permission_cache.containsKey(permission.getName()))
			return;
		permission_cache.put(permission.getName(), permission);
		permissions.add(permission);
		
		if (permission.getChildren().length > 0)
			for (NoxcraftPermission child : permission.getChildren())
				addPermission(child);
		
		if (permission.getParentNodes().length > 0)
			for (String node : permission.getParentNodes())
			{
				NoxcraftPermission perm = new NoxcraftPermission(node, "Parent node of " + permission.getName() + ".", PermissionDefault.OP);
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
			NoxcraftPermission perm = permission_cache.get(name);
			permission_cache.remove(name);
			permissions.remove(perm);
		} 
		else if (force)
		{
			NoxcraftPermission permFound = null;
			for (NoxcraftPermission perm : permissions)
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
	public final List<NoxcraftPermission> getAllNoxPerms()
	{
		return Collections.unmodifiableList(permissions);
	}
}
