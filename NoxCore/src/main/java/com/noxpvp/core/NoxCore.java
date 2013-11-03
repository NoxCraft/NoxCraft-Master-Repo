package com.noxpvp.core;

import java.util.ArrayList;
import java.util.Collections;
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
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.conversion.BasicConverter;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.listeners.ChestBlockListener;
import com.noxpvp.core.listeners.VoteListener;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.BaseReloader;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.CommandUtil;

public class NoxCore extends NoxPlugin {
	private static NoxCore instance;
	
	private VoteListener voteListener = null;
	private List<NoxPermission> permissions = new ArrayList<NoxPermission>();
	private transient WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>> permission_cache = new WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>>();
	
	private FileConfiguration config;

	private PlayerManager playerManager;
	
	private MasterReloader masterReloader = null;
	
	private static boolean useUserFile = true;
	private static boolean useNanoTime = false;
	
	/**
	 * @return the useNanoTime
	 */
	public static synchronized final boolean isUsingNanoTime() {
		return useNanoTime;
	}

	/**
	 * @param useNanoTime the useNanoTime to set
	 */
	public static synchronized final void setUseNanoTime(boolean useNanoTime) {
		NoxCore.useNanoTime = useNanoTime;
	}

	/**
	 * @return the useUserFile
	 */
	public static final boolean isUseUserFile() {
		return useUserFile;
	}

	/**
	 * @param useUserFile the useUserFile to set
	 */
	public static final void setUseUserFile(boolean useUserFile) {
		NoxCore.useUserFile = useUserFile;
	}

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
		if (voteListener != null)
			voteListener.destroy();
		
		setInstance(null);
		VaultAdapter.unload();
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
		
		masterReloader = new MasterReloader();
		
		Conversion.register(new BasicConverter<NoxPlayer>(NoxPlayer.class) {
			@Override
			protected NoxPlayer convertSpecial(Object object, Class<?> obType, NoxPlayer def) {
				if (object instanceof NoxPlayerAdapter)
					return ((NoxPlayerAdapter)object).getNoxPlayer();
				return def;
			}
		});
		
		voteListener = new VoteListener();
		getServer().getPluginManager().registerEvents(voteListener, this);
		
		VaultAdapter.load();
		
		// Serializable Objects
		ConfigurationSerialization.registerClass(SafeLocation.class);
		
		playerManager = new PlayerManager();
		Reloader r = new BaseReloader(masterReloader, "NoxCore") {
			public boolean reload() {
				return false;
			}
		};
		
		r.addModule(new BaseReloader(r, "config.yml") {
			public boolean reload() {
				NoxCore.this.reloadConfig();
				return true;
			}
		});
		
		addReloader(r);
	}
	
	@Override
	public void reloadConfig() {
		getCoreConfig().load();
		ChestBlockListener.isRemovingOnInteract = config.get("custom.events.chestblocked.isRemovingOnInteract", ChestBlockListener.isRemovingOnInteract);
		ChestBlockListener.usePlaceEvent = config.get("custom.events.chestblocked.usePlaceEvent", ChestBlockListener.usePlaceEvent);
		ChestBlockListener.useFormEvent = config.get("custom.events.chestblocked.useFormEvent", ChestBlockListener.useFormEvent);
	}
	
	public org.bukkit.configuration.file.FileConfiguration getConfig() {
		return config.getSource();
	}
	
	public FileConfiguration getCoreConfig(){
		if (config == null)
			config = new FileConfiguration(getDataFile("config.yml"));
		return config;
	}
	
	@Override
	public void saveConfig() {
		config.save();
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
	 * Acquires all permission nodes.
	 * 
	 * See addPermission and removePermission for manipulating this list.
	 * 
	 * @return an unmodifiable list of permissions
	 */
	public final List<NoxPermission> getAllNoxPerms()
	{
		return Collections.unmodifiableList(permissions);
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public Reloader getReloader(String path)
	{
		return masterReloader.getModule(path);
	}
	
	public boolean hasReloader(String path)
	{
		return masterReloader.hasModule(path);
	}

	public boolean hasReloaders()
	{
		return masterReloader.hasModules();
	}
	
	public boolean addReloader(Reloader r)
	{
		return masterReloader.addModule(r);
	}
	
	public static NoxCore getInstance() {
		return instance;
	}
	
	private static void setInstance(NoxCore instance)
	{
		NoxCore.instance = instance;
	}

}
