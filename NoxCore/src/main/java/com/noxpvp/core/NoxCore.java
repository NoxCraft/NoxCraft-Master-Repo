package com.noxpvp.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.conversion.BasicConverter;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.bergerkiller.bukkit.common.localization.ILocalizationDefault;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.*;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.listeners.ChestBlockListener;
import com.noxpvp.core.listeners.DeathListener;
import com.noxpvp.core.listeners.VoteListener;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.BaseReloader;
import com.noxpvp.core.reloader.Reloader;

public class NoxCore extends NoxPlugin {
	@SuppressWarnings("unchecked")
	private static final Class<CommandRunner>[] commands = (Class<CommandRunner>[]) new Class[]{ CoreCommand.class, ReloadCommand.class};

	private static NoxCore instance;
	
	private VoteListener voteListener = null;
	private List<NoxPermission> permissions = new ArrayList<NoxPermission>();
	private transient WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>> permission_cache = new WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>>();
	
	private FileConfiguration config;
	private FileConfiguration globalLocales;
	
	private PlayerManager playerManager;
	
	private MasterReloader masterReloader = null;

	private DeathListener deathListener;
	
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
	public void disable() {
		if (voteListener != null)
			voteListener.destroy();
		
		setInstance(null);
		VaultAdapter.unload();
	}
	
	@Override
	public void localization() {
		globalLocales = new FileConfiguration(this, "Global-Localization.yml");
		
		//Permission Locales
		loadGlobalLocale("permission.denied", "&4Permission Denied&r:&e %0%"); //%0% is the message while %1% is the perm node.
		loadGlobalLocale("permission.denied.verbose", getLocale("permission.denied", "%1%"));//Locale dynamic replace.
		
		//HOMES
		{ //Cleaner code.
				//Home List Locales
				loadGlobalLocale("homes.list.own", "&3Your Homes&r: &e%1%");
				loadGlobalLocale("homes.list", "&e%0%'s &3homes: &e%1%");

				//home Command
				loadGlobalLocale("homes.home.own", "&3You teleported to home: %1%");
				loadGlobalLocale("homes.home", "&3You teleported to %0%'s home named &e%1%");
				
				//delhome
				loadGlobalLocale("homes.delhome.own", "&cRemoved your home:&e%1%");
				loadGlobalLocale("homes.delhome", "&cDeleted &e%0%'s&c home named &e%1%");
				
				//Sethome
				loadGlobalLocale("homes.sethome.own", "&aSet new home named &e%1%&a at &6%2%");
				loadGlobalLocale("homes.sethome", "&aSet new home for &e%0%&2 &anamed: &e%1%&a at &6%2%");
				
				//Admin Commands
				//// NEED SOME LOCALES
				
				
				//Restrictors
				loadGlobalLocale("homes.warmup", "&3Warmup started. &cDo not move!"); //%0% is the time to warmup in seconds
				loadGlobalLocale("homes.cooldown", "&4Your too tired to get home.&e Wait another &4%0%&e seconds");
				
		}
		
		loadGlobalLocale("command.successful", "&2Successfully executed command: %0%");
		loadGlobalLocale("command.failed", "&4Failed to execute command: %0%");
		
		
		//Misc Command Locals
		loadGlobalLocale("console.needplayer", "This command requires a player: %0%");
		loadGlobalLocale("console.onlyplayer", "This command can only be run by a player.");
		
		//Error Locales
		loadGlobalLocale("error.null", "&4A null pointer error occured: &c%0%");
	}

	public void loadGlobalLocales(Class<? extends ILocalizationDefault> localizationDefaults) {
		for (ILocalizationDefault def : CommonUtil.getClassConstants(localizationDefaults))
			this.loadGlobalLocale(def);
	}
	
	public void loadGlobalLocale(ILocalizationDefault localizationDefault)
	{
		this.loadLocale(localizationDefault.getName(), localizationDefault.getDefault());
	}
	
	public void loadGlobalLocale(String path, String defaultValue) {
		path = path.toLowerCase(Locale.ENGLISH);
        if (!this.globalLocales.contains(path)) {
                this.globalLocales.set(path, defaultValue);
        }
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
		PluginManager pluginManager = getServer().getPluginManager();
		
		masterReloader = new MasterReloader();
		
		Conversion.register(new BasicConverter<NoxPlayer>(NoxPlayer.class) {
			@Override
			protected NoxPlayer convertSpecial(Object object, Class<?> obType, NoxPlayer def) {
				if (object instanceof NoxPlayerAdapter)
					return ((NoxPlayerAdapter)object).getNoxPlayer();
				return def;
			}
		});
		ConfigurationSerialization.registerClass(SafeLocation.class);
		
		voteListener = new VoteListener();
		deathListener = new DeathListener();
		pluginManager.registerEvents(voteListener, this);
		pluginManager.registerEvents(deathListener, this);
		
		VaultAdapter.load();
		
		// Serializable Objects
		
		playerManager = new PlayerManager();
		Reloader r = new BaseReloader(masterReloader, "NoxCore") {
			public boolean reload() {
				return false;
			}
		};
		
		registerAllCommands();
		
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
	public String getGlobalLocale(String path, String... arguments) {
        path = path.toLowerCase(Locale.ENGLISH);
        // First check if the path leads to a node
        if (this.globalLocales.isNode(path)) {
                // Redirect to the proper sub-node
                // Check recursively if the arguments are contained
                String newPath = path + ".default";
                if (arguments.length > 0) {
                        StringBuilder tmpPathBuilder = new StringBuilder(path);
                        String tmpPath = path;
                        for (int i = 0; i < arguments.length; i++) {
                                tmpPathBuilder.append('.');
                                if (arguments[i] == null) {
                                        tmpPathBuilder.append("null");
                                } else {
                                        tmpPathBuilder.append(arguments[i].toLowerCase(Locale.ENGLISH));
                                }
                                tmpPath = tmpPathBuilder.toString();
                                // New argument appended path exists, update the path
                                if (this.globalLocales.contains(tmpPath)) {
                                        newPath = tmpPath;
                                } else {
                                        break;
                                }
                        }
                }
                // Update path to lead to the new path
                path = newPath;
        }
        // Regular loading going on
        if (arguments.length > 0) {
                StringBuilder locale = new StringBuilder(this.globalLocales.get(path, ""));
                for (int i = 0; i < arguments.length; i++) {
                        StringUtil.replaceAll(locale, "%" + i + "%", LogicUtil.fixNull(arguments[i], "null"));
                }
                return locale.toString();
        } else {
                return this.globalLocales.get(path, String.class, "");
        }
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

	@Override
	public NoxCore getCore() {
		return (NoxCore)this;
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

	public MasterReloader getMasterReloader()
	{
		return masterReloader;
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
	
	private void registerAllCommands() {
		for (Class<CommandRunner> cls : commands)
		{
			SafeConstructor<CommandRunner> cons = new SafeConstructor<CommandRunner>(cls, new Class[0]);
			CommandRunner rn = cons.newInstance();
			if (rn != null)
				registerCommand(rn);
		}
	}
	
	public static NoxCore getInstance() {
		return instance;
	}
	
	private static void setInstance(NoxCore instance)
	{
		NoxCore.instance = instance;
	}

}
