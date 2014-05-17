package com.noxpvp.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.Command;
import com.noxpvp.core.commands.Command.CommandResult;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.CommandUtil;
import com.noxpvp.core.utils.gui.MessageUtil;

public abstract class NoxPlugin extends PluginBase {

	protected Map<String, Command> commandExecs = new HashMap<String, Command>();
	
	public void addPermission(NoxPermission perm) {
		NoxCore.getInstance().addPermission(perm);
	}
	
	public void addPermissions(NoxPermission... perms)
	{
		NoxCore.getInstance().addPermissions(perms);
	}
	
	@Override
	public boolean command(CommandSender sender, String command, String[] args) {
		String argLine = StringUtil.join(" ", args);
		CommandContext context = CommandUtil.parseCommand(sender, argLine);
		
		if (commandExecs.containsKey(command.toLowerCase(Locale.ENGLISH)))
		{
			Command cmd = commandExecs.get(command.toLowerCase(Locale.ENGLISH));
			if (cmd == null)
				throw new NullPointerException("Command execution class was null!");
			try {
				CommandResult result = cmd.executeCommand(context);
				
				if (!result.success) 
					result.executer.displayHelp(context.getSender());
				MessageUtil.sendMessage(context.getSender(), result.extraMessages);
				
				return true;
			} catch (NoPermissionException e) {
				MessageUtil.sendLocale(sender, GlobalLocale.FAILED_PERMISSION_VERBOSE, e.getMessage(), e.getPermission());
			} catch (NullPointerException e) {
				MessageUtil.sendLocale(sender, GlobalLocale.ERROR_NULL, e.getMessage());
				if (!(e instanceof SafeNullPointerException))
					e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public abstract NoxCore getCore();
	
	public String getGlobalLocale(String path, String... args)
	{
		NoxCore c;
		if ((c = getCore())==null)
			throw new IllegalStateException("This plugin depends on NoxCore! Its not loaded!");
		return c.getGlobalLocale(path, args);
	}
	
	/**
	* Gets a localization configuration node
	* 
	* @param path of the node to get
	* @return Localization configuration node
	*/
   public ConfigurationNode getGlobalLocalizationNode(String path) {
	   NoxCore c;
	   if ((c = getCore())==null)
		   throw new IllegalStateException("This plugin depends on NoxCore! Its not loaded!");
   
	   return c.getGlobalLocalizationNode(path);
   }
	
	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}
	
	public abstract PermissionHandler getPermissionHandler();
	
	public abstract Class<? extends ConfigurationSerializable>[] getSerialiables();
	
	public void registerCommand(Command runner)
	{
		if (runner == null)
			throw new IllegalArgumentException("Command Runner must not be null!");
		
		if (commandExecs.containsKey(runner.getName().toLowerCase(Locale.ENGLISH)))
			log(Level.WARNING, "Command - "+runner.getName() + " failed to register");
		
		commandExecs.put(runner.getName().toLowerCase(Locale.ENGLISH), runner);
		PluginCommand cmd = Bukkit.getPluginCommand(runner.getName().toLowerCase());
		try {
			cmd.setExecutor(this);
		} catch (NullPointerException e) {
			if (cmd == null && runner != null)
				log(Level.SEVERE, "Developer Error: Never put the command in plugins.yml\n"
						+ "\tCommand Name:" + runner.getName());
			else if (runner == null)
				log(Level.SEVERE, "Command runner was null!");
			else
			{
				log(Level.SEVERE, "Command Runner Failed to register. Null Pointer Exception. \n"
						+ "\tRunnerName: " + runner.getName() +"\n"
								+ "\tRunner Owner: "+ CommonUtil.getPluginByClass(runner.getClass()).getName());
			}
			e.printStackTrace();
		}
	}
	
	public void registerCommands(Collection<Command> runners)
	{
		for(Command runner : runners)
			registerCommand(runner);
	}
	
	public void registerCommands(Command... runners)
	{
		for (Command runner : runners)
			registerCommand(runner);
	}
	
	public final void register(Reloader reloader) {
		getMasterReloader().addModule(reloader);
	}

	/**
	 * @return Core Master Reloader
	 */
	public final MasterReloader getMasterReloader() {
		return MasterReloader.getInstance();
	}
}
