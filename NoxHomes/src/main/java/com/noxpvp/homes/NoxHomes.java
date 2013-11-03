package com.noxpvp.homes; 

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
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
import com.noxpvp.core.utils.CommandUtil;
import com.noxpvp.homes.commands.*;
import com.noxpvp.homes.tp.BaseHome;

public class NoxHomes extends NoxPlugin {

	NoxCore core;
	
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
		LocateHomeCommand.class,
		SetHomeCommand.class
	};
	public final static String HOMES_NODE = "homes";
	private static NoxHomes instance;
	private Map<String, CommandRunner> commandExecs;
	
	private HomeManager homeManager;
	
	@Override
	public boolean command(CommandSender sender, String command, String[] args) {
		Map<String, Object> flags = new LinkedHashMap<String, Object>();
		args = CommandUtil.parseFlags(flags, args);
		
		if (commandExecs.containsKey(command.toLowerCase(Locale.ENGLISH)))
		{
			CommandRunner cmd = commandExecs.get(command.toLowerCase(Locale.ENGLISH));
			if (cmd == null)
				throw new NullPointerException("Command Runner was null!");
			
			if (!cmd.execute(sender, flags, args))
				cmd.displayHelp(sender);
			
			return true;
		}
		return false;
	}
	
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
		
		instance = this;
		core = NoxCore.getInstance();
		
		registerAllCommands();
		
		commandExecs = new HashMap<String, CommandRunner>();
		homeManager = new HomeManager();
		
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
								new NoxPermission(this, "homes.admin.history", "Ability to read Home History.", PermissionDefault.OP),
								new NoxPermission(this, "homes.admin.wipe.*", "Permission to wipe all data.", PermissionDefault.OP,
										new NoxPermission(this, "homes.admin.wipe.history", "Permission to wipe history.", PermissionDefault.OP),
										new NoxPermission(this, "homes.admin.wipe.homes", "Permission to wipe homes data.", PermissionDefault.OP)
								),
								new NoxPermission(this, "homes.admin.load", "Permission to reload plugin.", PermissionDefault.OP),
								new NoxPermission(this, "homes.admin.save", "Permission to force save.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, "admin",HomeAdminImportCommand.PERM_NODE), "Permission to migrate save data to and from plugin.", PermissionDefault.OP)
						),
						new NoxPermission(this, "homes.sethome.*", "Allowed to set home of everything including others.", PermissionDefault.FALSE,
								new NoxPermission(this, "homes.sethome.default", "Allowed to set default home.", PermissionDefault.OP),
								new NoxPermission(this, "homes.sethome.named", "Allowed to set named homes.", PermissionDefault.OP),
								new NoxPermission(this, "homes.sethome.others.*", "Allowed to set any type of other peoples homes.", PermissionDefault.FALSE,
										new NoxPermission(this, "homes.sethome.others.default", "Allowed to set other peoples default homes.", PermissionDefault.OP),
										new NoxPermission(this, "homes.sethome.others.named", "Allowed to set other peoples named homes.", PermissionDefault.OP)
								)
						),
						new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, HomeCommand.PERM_NODE, "*"), "Allowed to warp to any type of home. Including others.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, HomeCommand.PERM_NODE, "default"), "Allowed to warp to your default home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, HomeCommand.PERM_NODE, "named"), "Allowed to warp to your named home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "*"), "Allowed to warp to others homes. Without invitiation.", PermissionDefault.FALSE,
										new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, HomeCommand.PERM_NODE, "others","default"), "Allowed to warp to other peoples default home's.", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "named"), "Allowed to warp to other peoples named home's", PermissionDefault.OP)
								)
						),
						new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "*"), "Allowed to delete any and all homes.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "default"), "Allowed to delete your default home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "named"), "Allowed to delete your named home.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "*"), "Allowed to delete any type of other people's homes.", PermissionDefault.FALSE,
										new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "default"), "Allowed to delete other peoples default home's", PermissionDefault.OP),
										new NoxPermission(this, StringUtil.combine(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "named"), "Allowed to delete other peoples named home's", PermissionDefault.OP)
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
						new NoxPermission(this, StringUtil.combine(".", "homes", HomeListCommand.LIST_PERM_NODE, "*"), "Allows to list any home of any person.", PermissionDefault.FALSE,
								new NoxPermission(this, StringUtil.combine(".", "homes", HomeListCommand.LIST_PERM_NODE, "own"), "List your own homes.", PermissionDefault.OP),
								new NoxPermission(this, StringUtil.combine(".", "homes", HomeListCommand.LIST_PERM_NODE, "others"), "List other people's homes.", PermissionDefault.OP)
						)
				)
			);
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

}
