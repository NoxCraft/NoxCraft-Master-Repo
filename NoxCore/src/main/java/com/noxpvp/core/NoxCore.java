package com.noxpvp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.Common;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.utils.CommandUtil;

public class NoxCore extends NoxPlugin {
	private static NoxCore instance;
	
	private List<NoxPermission> permissions = new ArrayList<NoxPermission>();
	private transient WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>> permission_cache = new WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>>();
	
	private Map<String, CommandRunner> commandExecs;
	
	@Override
	public boolean command(CommandSender sender, String command, String[] args) {
		Map<String, Object> flags = new LinkedHashMap<String, Object>();
		args = CommandUtil.parseFlags(flags, args);
		
		if (commandExecs.containsKey(command.toLowerCase(Locale.ENGLISH)))
			return commandExecs.get(command.toLowerCase(Locale.ENGLISH)).execute(sender, flags, args);
		else
			return false;
	}
	
	@Override
	public void disable() {
		setInstance(null);
	}
	
	@Override
	public void localization() {
		//Permission Locales
		loadLocale("permission.denied", "&4Permission Denied&r:&e %0%"); //%0% is the message while %1% is the perm node.
		loadLocale("permission.denied.verbose", getLocale("permission.denied", "%1%"));//Locale dynamic replace.
		
		//HOMES
		{ //Cleaner code.
				//Home List Locales
				loadLocale("homes.list.own", "&3Your Homes&r: &e%1%");
				loadLocale("homes.list", "&e%0%'s &3homes: &e%1%");

				//home Command
				loadLocale("homes.home.own", "&3You teleported to home: %1%");
				loadLocale("homes.home", "&3You teleported to %0%'s home named &e%1%");
				
				//delhome
				loadLocale("homes.delhome.own", "&cRemoved your home:&e%1%");
				loadLocale("homes.delhome", "&cDeleted &e%0%'s&c home named &e%1%");
				
				//Sethome
				loadLocale("homes.sethome.own", "&aSet new home named &e%1%&a at &6%2%");
				loadLocale("homes.sethome", "&aSet new home for &e%0%&2 &anamed: &e%1%&a at &6%2%");
				
				//Admin Commands
				//// NEED SOME LOCALES
				
				
				//Restrictors
				loadLocale("homes.warmup", "&3Warmup started. &cDo not move!"); //%0% is the time to warmup in seconds
				loadLocale("homes.cooldown", "&4Your too tired to get home.&e Wait another &4%0%&e seconds");
				
		}
		
		loadLocale("command.successful", "&2Successfully executed command: %0%");
		loadLocale("command.failed", "&4Failed to execute command: %0%");
		
		
		//Misc Command Locals
		loadLocale("console.needplayer", "This command requires a player: %0%");
		loadLocale("console.onlyplayer", "This command can only be run by a player.");
		
		//Error Locales
		loadLocale("error.null", "&4A null pointer error occured: &c%0%");
	}

	@Override
	public void enable() {
		if (instance != null)
		{
			log(Level.SEVERE, "This plugin already has an instance running!! Disabling second run.");
			setEnabled(false);
			return;
		}
		
		setInstance(this);
		
		commandExecs = new HashMap<String, CommandRunner>();
		
		// Serializable Objects
		ConfigurationSerialization.registerClass(SafeLocation.class);
	}
	
	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}

	@Override
	public void permissions() {
		addPermission( //Currently does nothing.
			new NoxPermission(this, "core.*", "All noxcore permissions (Including admin nodes).", PermissionDefault.OP,
					new NoxPermission(this, "core.reload", "Reload command for Nox Core", PermissionDefault.OP),
					new NoxPermission(this, "core.save", "Save permission for saving everything in core.", PermissionDefault.OP),
					new NoxPermission(this, "core.load", "Load permission for loading everything in core.", PermissionDefault.OP)
			)
		);
	}

	@Override
	public void addPermissions(NoxPermission... perms)
	{
		for (NoxPermission perm : perms)
			addPermission(perm);
	}

	@Override
	public void addPermission(NoxPermission permission)
	{
		NoxPlugin plugin = permission.getPlugin();
		if (!permission_cache.containsKey(plugin))
			permission_cache.put(plugin, new WeakHashMap<String, NoxPermission>());
		
		Map<String, NoxPermission> cache = permission_cache.get(plugin);
		
		if (cache == null)
		{
			log(Level.WARNING, new StringBuilder().append("Failed to initialize plugin reference for permission for plugin ").append(plugin.getName()).append("! Could not cache permissions for that plugin!").toString());
			return;
		}
		
		if (cache.containsKey(permission.getName()))
			return;
		cache.put(permission.getName(), permission);
		permissions.add(permission);
		if (permission.getChildren().length > 0)
			addPermissions(permission.getChildren());
		
		
//		if (permission_cache.containsKey(permission.getName()))
//			return;
//		permission_cache.put(permission.getName(), permission);
//		permissions.add(permission);
//
//		if (permission.getChildren().length > 0)
//			addPermissions(permission.getChildren());

//		if (permission.getParentNodes().length > 0) //Should not be needed since we know we are creating parents before we create nodes. Unless someone develops plugin ontop of this plugin that is not our developers.
//			for (String node : permission.getParentNodes())
//			{
//				NoxPermission perm = new NoxPermission(node, "Parent node of " + permission.getName() + ".", PermissionDefault.OP);
//				addPermission(plugin, perm);
//			}

		plugin.loadPermission(permission);
	}

	public void removePermission(NoxPlugin plugin, String name)
	{
		removePermission(plugin, name, false);
	}

	public void removePermission(NoxPlugin plugin, String name, boolean force)
	{
		if (!permission_cache.containsKey(plugin))
			return;
		
		Map<String, NoxPermission> cache = permission_cache.get(plugin);
		
		if (cache.containsKey(name))
		{
			Bukkit.getPluginManager().removePermission(name);
			
			NoxPermission perm = cache.get(name);
			
			cache.remove(name);
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
	
	public void registerCommands(Collection<CommandRunner> runners)
	{
		for(CommandRunner runner : runners)
			registerCommand(runner);
	}
	
	public void registerCommands(CommandRunner... runners)
	{
		for (CommandRunner runner : runners)
			registerCommand(runner);
	}
	
	public void registerCommand(CommandRunner runner)
	{
		if (commandExecs.containsKey(runner.getName()))
			return;
		else
			commandExecs.put(runner.getName(), runner);
	}
	
	private static void setInstance(NoxCore instance)
	{
		NoxCore.instance = instance;
	}
}
