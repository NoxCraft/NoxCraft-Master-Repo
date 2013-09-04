package net.noxcraft.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;

import net.noxcraft.core.permissions.NoxPermission;
import net.noxcraft.core.tp.BaseHome;
import net.noxcraft.core.utils.CommandUtil;

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
		Map<String, Object> flags = new LinkedHashMap<String, Object>();
		args = CommandUtil.parseFlags(flags, args);
		
		
		return false;
		
	}

	@Override
	public void disable() {
		instance = null;
	}

	@Override
	public void enable() {
		instance = this;

		// Serializable Objects
		ConfigurationSerialization.registerClass(BaseHome.class);
		ConfigurationSerialization.registerClass(SafeLocation.class);

		// Not ready to enable...
		log(Level.SEVERE, "This plugin has no functionality. Why are we running this? Self Disabling...");
		setEnabled(false);

	}

	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}

	@Override
	public void permissions() {
//		addPermission( //Currently does nothing..
//			new NoxPermission("core.*", "All noxcore permissions (Including admin nodes).", PermissionDefault.OP,
//					new NoxPermission("core.reload", "Reload command for Nox Core", PermissionDefault.OP),
//					new NoxPermission("core.save", "Save permission for saving everything in core.", PermissionDefault.OP),
//					new NoxPermission("core.load", "Load permission for loading everything in core.", PermissionDefault.OP)
//			)
//		);
		
		addPermission(
			new NoxPermission("homes", "All of NoxHomes permissions Including admin.", PermissionDefault.FALSE,
					new NoxPermission("homes.admin", "All NoxHome's Admin Permissions", PermissionDefault.FALSE, 
							new NoxPermission("homes.admin.history", "Ability to read Home History.", PermissionDefault.OP),
							new NoxPermission("homes.admin.wipe", "Permission to wipe all data.", PermissionDefault.OP,
									new NoxPermission("homes.admin.wipe.history", "Permission to wipe history", PermissionDefault.OP)
							),
							new NoxPermission("homes.admin.load", "Permission to reload plugin.", PermissionDefault.OP),
							new NoxPermission("homes.admin.save", "Permission to force save.", PermissionDefault.OP),
							new NoxPermission("homes.admin.migrate", "Permission to migrate save data to and from plugin.", PermissionDefault.OP,
									new NoxPermission("homes.admin.migrate.to", "Permission to migrate into this plugin.", PermissionDefault.OP),
									new NoxPermission("homes.admin.migrate.from", "Permission to migrate from this plugin.", PermissionDefault.OP)
							)
					),
					new NoxPermission("homes.sethome", "Allowed to set home of everything including others.", PermissionDefault.FALSE,
							new NoxPermission("homes.sethome.default", "Allowed to set default home.", PermissionDefault.OP),
							new NoxPermission("homes.sethome.named", "Allowed to set named homes.", PermissionDefault.OP),
							new NoxPermission("homes.sethome.others", "Allowed to set any type of other peoples homes.", PermissionDefault.FALSE,
									new NoxPermission("homes.sethome.others.default", "Allowed to set other peoples default homes.", PermissionDefault.OP),
									new NoxPermission("homes.sethome.others.named", "Allowed to set other peoples named homes.", PermissionDefault.OP)
							)
					),
					new NoxPermission("homes.home", "Allowed to warp to any type of home. Including others.", PermissionDefault.FALSE,
							new NoxPermission("homes.home.default", "Allowed to warp to your default home.", PermissionDefault.OP),
							new NoxPermission("homes.home.named", "Allowed to warp to your named home.", PermissionDefault.OP),
							new NoxPermission("homes.home.others", "Allowed to warp to others homes. Without invitiation.", PermissionDefault.FALSE,
									new NoxPermission("homes.home.others.default", "Allowed to warp to other peoples default home's.", PermissionDefault.OP),
									new NoxPermission("homes.home.others.named", "Allowed to warp to other peoples named home's", PermissionDefault.OP)
							)
					),
					new NoxPermission("homes.delhome", "Allowed to delete any and all homes.", PermissionDefault.FALSE,
							new NoxPermission("homes.delhome.default", "Allowed to delete your default home.", PermissionDefault.OP),
							new NoxPermission("homes.delhome.named", "Allowed to delete your named home.", PermissionDefault.OP),
							new NoxPermission("homes.delhome.others", "Allowed to delete any type of other people's homes.", PermissionDefault.FALSE,
									new NoxPermission("homes.delhome.others.default", "Allowed to delete other peoples default home's", PermissionDefault.OP),
									new NoxPermission("homes.delhome.others.named", "Allowed to delete other peoples named home's", PermissionDefault.OP)
							)
					),
					new NoxPermission("homes.invite", "Allowed to set invites to any home of anyone.", PermissionDefault.FALSE,
							new NoxPermission("homes.invite.default", "Allowed to invite others into your default home.", PermissionDefault.OP),
							new NoxPermission("homes.invite.named", "Allowed to invite others into your named home.", PermissionDefault.OP),
							new NoxPermission("homes.invite.others", "Allowed to invite others into other people's homes.", PermissionDefault.FALSE,
									new NoxPermission("homes.invite.others.default", "Allowed to invite others into other people's default home.", PermissionDefault.OP),
									new NoxPermission("homes.invite.others.named", "Allowed to invite others into other people's named home's." , PermissionDefault.OP)
							)
					),
					new NoxPermission("homes.locate", "Allow you to locate any and all homes.", PermissionDefault.FALSE,
							new NoxPermission("homes.locate.default", "Allows you to retrieve coords and set compass to your default home.", PermissionDefault.OP,
									new NoxPermission("homes.locate.default.compass", "Allows you to use a compass to locate your home.", PermissionDefault.OP),
									new NoxPermission("homes.locate.default.coords", "Allows retrieval and display of the coords of your home.", PermissionDefault.OP)
							),
							new NoxPermission("homes.locate.named", "Allows you to retrieve coords and set compass to your named home.", PermissionDefault.OP,
									new NoxPermission("homes.locate.named.compass", "Allows you to use a compass to locate your home.", PermissionDefault.OP),
									new NoxPermission("homes.locate.named.coords", "Allows retrieval and display of the coords of your home.", PermissionDefault.OP)
							),
							new NoxPermission("homes.locate.others", "Allows you to locate any type of other people's homes.", PermissionDefault.FALSE,
									new NoxPermission("homes.locate.others.default", "Allows you to locate other's Default homes.", PermissionDefault.OP,
											new NoxPermission("homes.locate.others.default.compass", "Allows you to locate others Default home with a compass.", PermissionDefault.OP),
											new NoxPermission("homes.locate.others.default.compass", "Allows you to locate others Default home with coords.", PermissionDefault.OP)
									),
									new NoxPermission("homes.locate.others.named", "Allows you to locate others Named homes.", PermissionDefault.OP,
											new NoxPermission("homes.locate.others.named.compass", "Allows you to locate others Named home with a compass.", PermissionDefault.OP),
											new NoxPermission("homes.locate.others.named.coords", "Allows you to locate others Named home with coords.", PermissionDefault.OP)
									)
							)
					)
			)
		);
			
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
	 * 
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
