package com.noxpvp.homes; 

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.BaseReloader;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.PermissionHandler;
import com.noxpvp.homes.commands.DeleteHomeCommand;
import com.noxpvp.homes.commands.HomeAdminCommand;
import com.noxpvp.homes.commands.HomeAdminImportCommand;
import com.noxpvp.homes.commands.HomeCommand;
import com.noxpvp.homes.commands.HomeListCommand;
import com.noxpvp.homes.commands.SetHomeCommand;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;

public class NoxHomes extends NoxPlugin {

	NoxCore core;
	
	private PermissionHandler permHandler;
	
	public static NoxHomes getInstance() {
		return instance;
	}
	
	/**
	 * <b> BE SURE TO HAVE THOSE CLASSES IMPLEMENT <u>CommandRunner</u> OR YOU RISK CRASH!</b>
	 */
	@SuppressWarnings("unchecked")
	private static final Class<CommandRunner>[] commands = (Class<CommandRunner>[]) new Class<?>[] {
		DeleteHomeCommand.class,
		HomeAdminCommand.class,
		HomeCommand.class,
		HomeListCommand.class,
//		LocateHomeCommand.class,
		SetHomeCommand.class
	};
	
	public static final String HOMES_NODE = "nox.homes";
	private static NoxHomes instance;
	
	private HomeLimitManager limitManager;
	private HomeManager homeManager;
	
	@Override
	public void disable() {
		setInstance(null);
	}

	private static void setInstance(NoxHomes homes) {
		instance = homes;
	}

	@Override
	public void enable() {
		if (instance != null)
		{
			log(Level.SEVERE, "Instance already running of NoxHomes!");
			log(Level.SEVERE, "Self Disabling new instance.");
			setEnabled(false);
			return;
		}
		
		permHandler = new PermissionHandler(this);
		
		instance = this;
		core = NoxCore.getInstance();
		
		
		commandExecs = new HashMap<String, CommandRunner>();
		homeManager = new HomeManager(NoxCore.getInstance());
		limitManager = new HomeLimitManager();
		
		homeManager.load();
		limitManager.load();
		
		ConfigurationSerialization.registerClass(BaseHome.class);
		
		Reloader r = new BaseReloader(core.getMasterReloader(), "NoxHomes") {
			public boolean reload() {
				reloadConfig();
				return true;
			}
		};
		
		r.addModule(new BaseReloader(r, "homes") {
			public boolean reload() {
				getHomeManager().load();
				return true;
			}
		});
		
		r.addModule(new BaseReloader(r, "limits") {
			public boolean reload() {
				getLimitsManager().load();
				return true;
			}
		});
		
		core.addReloader(r);
		registerAllCommands();
	}
	
	public NoxCore getCore() {
		return core;
	}
	
	public HomeManager getHomeManager()
	{
		return homeManager;
	}

	@Override
	public void permissions() {
		addPermission(
				new NoxPermission(this, "homes.*", "All of NoxHomes permissions Including admin.", PermissionDefault.FALSE,
						new NoxPermission(this, "homes.admin.*", "All NoxHome's Admin Permissions", PermissionDefault.FALSE, 
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "history"), "Ability to read Home History.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "wipe" , "*"), "Permission to wipe all data.", PermissionDefault.OP,
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "wipe" , "history"), "Permission to wipe history.", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "wipe" , "homes"), "Permission to wipe homes data.", PermissionDefault.OP)
								),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "load"), "Permission to reload plugin.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", "save"), "Permission to force save.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, "admin", HomeAdminImportCommand.PERM_NODE), "Permission to migrate save data to and from plugin.", PermissionDefault.OP)
						),
						new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "*"), "Allowed to set home of everything including others.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, DefaultHome.PERM_NODE), "Allowed to set default home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, NamedHome.PERM_NODE), "Allowed to set named homes.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", "*"), "Allowed to set any type of other peoples homes.", PermissionDefault.FALSE,
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", DefaultHome.PERM_NODE), "Allowed to set other peoples default homes.", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", NamedHome.PERM_NODE), "Allowed to set other peoples named homes.", PermissionDefault.OP)
								)
						),
						new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "*"), "Allowed to warp to any type of home. Including others.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "default"), "Allowed to warp to your default home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "named"), "Allowed to warp to your named home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "*"), "Allowed to warp to others homes. Without invitiation.", PermissionDefault.FALSE,
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others","default"), "Allowed to warp to other peoples default home's.", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "named"), "Allowed to warp to other peoples named home's", PermissionDefault.OP)
								)
						),
						new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "*"), "Allowed to delete any and all homes.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "default"), "Allowed to delete your default home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "named"), "Allowed to delete your named home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "*"), "Allowed to delete any type of other people's homes.", PermissionDefault.FALSE,
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "default"), "Allowed to delete other peoples default home's", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "named"), "Allowed to delete other peoples named home's", PermissionDefault.OP)
								)
						),
//						new NoxPermission(this, "homes.invite.*", "Allowed to set invites to any home of anyone.", PermissionDefault.FALSE,
//								new NoxPermission(this, "homes.invite.default", "Allowed to invite others into your default home.", PermissionDefault.OP),
//								new NoxPermission(this, "homes.invite.named", "Allowed to invite others into your named home.", PermissionDefault.OP),
//								new NoxPermission(this, "homes.invite.others.*", "Allowed to invite others into other people's homes.", PermissionDefault.FALSE,
//										new NoxPermission(this, "homes.invite.others.default", "Allowed to invite others into other people's default home.", PermissionDefault.OP),
//										new NoxPermission(this, "homes.invite.others.named", "Allowed to invite others into other people's named home's." , PermissionDefault.OP)
//								)
//						),
						new NoxPermission(this, "homes.locate.*", "Allow you to locate any and all homes.", PermissionDefault.FALSE,
								new NoxPermission(this, "homes.locate.default.*", "Allows you to retrieve coords and set compass to your default home.", PermissionDefault.OP,
										new NoxPermission(this, "homes.locate.default.compass", "Allows you to use a compass to locate your home.", PermissionDefault.OP),
										new NoxPermission(this, "homes.locate.default.coords", "Allows retrieval and display of the coords of your home.", PermissionDefault.OP)
								),
								new NoxPermission(this, "homes.locate.named.*", "Allows you to retrieve coords and set compass to your named home.", PermissionDefault.OP,
										new NoxPermission(this, "homes.locate.named.compass", "Allows you to use a compass to locate your home.", PermissionDefault.OP),
										new NoxPermission(this, "homes.locate.named.coords", "Allows retrieval and display of the coords of your home.", PermissionDefault.OP)
								),
								new NoxPermission(this, "homes.locate.others.*", "Allows you to locate any type of other people's homes.", PermissionDefault.FALSE,
										new NoxPermission(this, "homes.locate.others.default.*", "Allows you to locate other's Default homes.", PermissionDefault.OP,
												new NoxPermission(this, "homes.locate.others.default.compass", "Allows you to locate others Default home with a compass.", PermissionDefault.OP),
												new NoxPermission(this, "homes.locate.others.default.compass", "Allows you to locate others Default home with coords.", PermissionDefault.OP)
										),
										new NoxPermission(this, "homes.locate.others.named.*", "Allows you to locate others Named homes.", PermissionDefault.OP,
												new NoxPermission(this, "homes.locate.others.named.compass", "Allows you to locate others Named home with a compass.", PermissionDefault.OP),
												new NoxPermission(this, "homes.locate.others.named.coords", "Allows you to locate others Named home with coords.", PermissionDefault.OP)
										)
								)
						),
						new NoxPermission(this, StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "*"), "Allows to list any home of any person.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "own"), "List your own homes.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "others"), "List other people's homes.", PermissionDefault.OP)
						)
				)
			);
	}
	
	@Override
	public void localization() {
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

	private void registerAllCommands() {
		for (Class<CommandRunner> cls : commands)
		{
			SafeConstructor<CommandRunner> cons = new SafeConstructor<CommandRunner>(cls, new Class[0]);
			CommandRunner rn = cons.newInstance();
			if (rn != null)
				registerCommand(rn);
		}
	}

	public HomeLimitManager getLimitsManager() {
		return limitManager;
	}

	@Override
	public PermissionHandler getPermissionHandler() {
		return permHandler;
	}
}
