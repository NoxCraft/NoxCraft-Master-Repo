package com.noxpvp.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.conversion.BasicConverter;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.bergerkiller.bukkit.common.localization.ILocalizationDefault;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.Command;
import com.noxpvp.core.commands.ReloadCommand;
import com.noxpvp.core.data.CoolDown;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.listeners.ChatPingListener;
import com.noxpvp.core.listeners.ChestBlockListener;
import com.noxpvp.core.listeners.DeathListener;
import com.noxpvp.core.listeners.LoginListener;
import com.noxpvp.core.listeners.OnLogoutSaveListener;
import com.noxpvp.core.listeners.VoteListener;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.BaseReloader;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.PermissionHandler;

public class NoxCore extends NoxPlugin {
	private ChatPingListener chatPingListener;

	private FileConfiguration config;
	
	private DeathListener deathListener;
	private FileConfiguration globalLocales;
	private LoginListener loginListener;
	
	private MasterReloader masterReloader = null;
	
	private PermissionHandler permHandler;
	private transient WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>> permission_cache = new WeakHashMap<NoxPlugin, WeakHashMap<String, NoxPermission>>();
	
	private List<NoxPermission> permissions = new ArrayList<NoxPermission>();
	
	private PlayerManager playerManager = null;
	private OnLogoutSaveListener saveListener;

	private VoteListener voteListener = null;
	
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
	@Override
	public void addPermissions(NoxPermission... perms)
	{
		for (NoxPermission perm : perms)
			addPermission(perm);
	}
	
	public boolean addReloader(Reloader r)
	{
		return masterReloader.addModule(r);
	}
	@Override
	public void disable() {
		if (voteListener != null)
			voteListener.destroy();
		
		playerManager.save();
		
		HandlerList.unregisterAll(this);
		setInstance(null);
		VaultAdapter.unload();
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
		
		registerSerials(this);
		for (Plugin p : CommonUtil.getPlugins())
			if (p instanceof NoxPlugin && CommonUtil.isDepending(p, this))
				registerSerials((NoxPlugin)p);
		
		playerManager = new PlayerManager();
		
		permHandler = new PermissionHandler(this);
		
		masterReloader = new MasterReloader();
		
		Conversion.register(new BasicConverter<NoxPlayer>(NoxPlayer.class) {
			@Override
			protected NoxPlayer convertSpecial(Object object, Class<?> obType, NoxPlayer def) {
				if (object instanceof NoxPlayerAdapter)
					return ((NoxPlayerAdapter)object).getNoxPlayer();
				return def;
			}
		});
		
		
		chatPingListener = new ChatPingListener();
		voteListener = new VoteListener();
		deathListener = new DeathListener();
		loginListener = new LoginListener();
		saveListener = new OnLogoutSaveListener(this); 
		
		chatPingListener.register();
		
		if (CommonUtil.isPluginEnabled("Votifier")) //Fixes console error message.
			voteListener.register();
		
		saveListener.register();
		deathListener.register();
		loginListener.register();
		
		CommonUtil.queueListenerLast(loginListener, PlayerLoginEvent.class);
		VaultAdapter.load();
		
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
		
		r.addModule(new BaseReloader(r, "group-name") {
			
			public boolean reload() {
				VaultAdapter.GroupUtils.reloadGroupNames();
				return true;
			}
		});
		
		addReloader(r);
		
		
		// ==== Localization ====
        if (!this.globalLocales.isEmpty()) {
                this.saveGlobalLocalization();
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
	public org.bukkit.configuration.file.FileConfiguration getConfig() {
		return getCoreConfig().getSource();
	}
	
	@Override
	public NoxCore getCore() {
		return (NoxCore)this;
	}
	
	public FileConfiguration getCoreConfig(){
		if (config == null)
			config = new FileConfiguration(getDataFile("config.yml"));
		return config;
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
	public ConfigurationNode getGlobalLocalizationNode(String path) {
		return this.globalLocales.getNode(path);
	}

	public MasterReloader getMasterReloader()
	{
		return masterReloader;
	}
	
	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}
	
	public PermissionHandler getPermissionHandler() {
		return permHandler;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public Reloader getReloader(String path)
	{
		return masterReloader.getModule(path);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends ConfigurationSerializable>[] getSerialiables() {
		return new Class[]{SafeLocation.class, CoolDown.class};
	}

	public boolean hasReloader(String path)
	{
		return masterReloader.hasModule(path);
	}
	
	public boolean hasReloaders()
	{
		return masterReloader.hasModules();
	}
	
	public void loadGlobalLocale(ILocalizationDefault localizationDefault)
	{
		this.loadGlobalLocale(localizationDefault.getName(), localizationDefault.getDefault());
	}
	
	public void loadGlobalLocale(String path, String defaultValue) {
		path = path.toLowerCase(Locale.ENGLISH);
        if (!this.globalLocales.contains(path)) {
                this.globalLocales.set(path, defaultValue);
        }
	}
	
	public void loadGlobalLocales(Class<? extends ILocalizationDefault> localizationDefaults) {
		for (ILocalizationDefault def : CommonUtil.getClassConstants(localizationDefaults))
			this.loadGlobalLocale(def);
	}
	
	public final void loadGlobalLocalization() {
		this.globalLocales.load();
	}
	
	@Override
	public void localization() {
		
		
		globalLocales = new FileConfiguration(this, "Global-Localization.yml");
		
		
		// load
        if (this.globalLocales.exists()) {
                this.loadLocalization();
        }

        // header
        this.globalLocales.setHeader("Below are the global localization nodes set for Nox Plugins '" + this.getName() + "'.");
        this.globalLocales.addHeader("For colors, use the & character followed up by 0 - F");
        this.globalLocales.addHeader("Need help with this file? Please visit:");
        this.globalLocales.addHeader("http://dev.bukkit.org/server-mods/bkcommonlib/pages/general/localization/");

		
		loadGlobalLocales(GlobalLocale.class);
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

	private void registerAllCommands() {
		for (Class<Command> cls : commands)
		{
			SafeConstructor<Command> cons = new SafeConstructor<Command>(cls, new Class[0]);
			Command rn = cons.newInstance();
			if (rn != null)
				registerCommand(rn);
		}
	}

	private void registerSerials(NoxPlugin p) {
		if (p.getSerialiables() != null) {
			log(Level.INFO, new StringBuilder().append("Attempting to load ").append(p.getSerialiables().length).append(" '").append(p.getName()).append("' serializables.").toString());
			
			for (Class<? extends ConfigurationSerializable> c : p.getSerialiables())
				ConfigurationSerialization.registerClass(c);
		}
	}

	@Override
	public void reloadConfig() {
		getCoreConfig().load();
		VaultAdapter.GroupUtils.setGroupList(config.get("group.priorities", new LinkedList<String>()));
		
		ChestBlockListener.isRemovingOnInteract = config.get("custom.events.chestblocked.isRemovingOnInteract", ChestBlockListener.isRemovingOnInteract);
		ChestBlockListener.usePlaceEvent = config.get("custom.events.chestblocked.usePlaceEvent", ChestBlockListener.usePlaceEvent);
		ChestBlockListener.useFormEvent = config.get("custom.events.chestblocked.useFormEvent", ChestBlockListener.useFormEvent);
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
	public void saveConfig() {
		config.set("group.priorities", VaultAdapter.GroupUtils.getGroupList());

		config.set("custom.events.chestblocked.isRemovingOnInteract", ChestBlockListener.isRemovingOnInteract);
		config.set("custom.events.chestblocked.usePlaceEvent", ChestBlockListener.usePlaceEvent);
		config.set("custom.events.chestblocked.useFormEvent", ChestBlockListener.useFormEvent);
		
		config.save();
	}

	public final void saveGlobalLocalization() {
		this.globalLocales.save();
	}

	@SuppressWarnings("unchecked")
	private static final Class<Command>[] commands = (Class<Command>[]) new Class[]{ /*CoreCommand.class,*/ ReloadCommand.class};
	
	private static NoxCore instance;
	
	private static boolean useNanoTime = false;

	private static boolean useUserFile = true;
	
	public static NoxCore getInstance() {
		return instance;
	}
	
	/**
	 * @return the useUserFile
	 */
	public static final boolean isUseUserFile() {
		return useUserFile;
	}
	
	/**
	 * @return the useNanoTime
	 */
	public static synchronized final boolean isUsingNanoTime() {
		return useNanoTime;
	}
	
	private static void setInstance(NoxCore instance)
	{
		NoxCore.instance = instance;
	}
	
	/**
	 * @param useNanoTime the useNanoTime to set
	 */
	public static synchronized final void setUseNanoTime(boolean useNanoTime) {
		NoxCore.useNanoTime = useNanoTime;
	}
	/**
	 * @param useUserFile the useUserFile to set
	 */
	public static final void setUseUserFile(boolean useUserFile) {
		NoxCore.useUserFile = useUserFile;
	}
}