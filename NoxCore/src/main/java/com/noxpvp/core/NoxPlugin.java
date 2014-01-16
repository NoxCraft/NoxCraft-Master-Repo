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
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.utils.CommandUtil;
import com.noxpvp.core.utils.MessageUtil;
import com.noxpvp.core.utils.PermissionHandler;

public abstract class NoxPlugin extends PluginBase {

	protected Map<String, CommandRunner> commandExecs = new HashMap<String, CommandRunner>();
	
	protected void addPermission(NoxPermission perm) {
		NoxCore.getInstance().addPermission(perm);
	}
	
	protected void addPermissions(NoxPermission... perms)
	{
		NoxCore.getInstance().addPermissions(perms);
	}
	
	@Override
	public boolean command(CommandSender sender, String command, String[] args) {
		String argLine = StringUtil.join(" ", args);
		CommandContext context = CommandUtil.parseCommand(sender, argLine);
		
		if (commandExecs.containsKey(command.toLowerCase(Locale.ENGLISH)))
		{
			CommandRunner cmd = commandExecs.get(command.toLowerCase(Locale.ENGLISH));
			if (cmd == null)
				throw new NullPointerException("Command Runner was null!");
			try {
				if (!cmd.execute(context))
					cmd.displayHelp(sender);
			} catch (NoPermissionException e) {
				MessageUtil.sendLocale(e.getSender(), GlobalLocale.FAILED_PERMISSION_VERBOSE, e.getMessage(), e.getPermission());
			}
			return true;
		}
		return false;
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
	
	public abstract NoxCore getCore();
	
	public String getGlobalLocale(String path, String... args)
	{
		NoxCore c;
		if ((c = getCore())==null)
			throw new IllegalStateException("This plugin depends on NoxCore! Its not loaded!");
		return c.getGlobalLocale(path, args);
	}
	
	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}
	
	public void registerCommand(CommandRunner runner)
	{
		if (runner == null)
			throw new IllegalArgumentException("Command Runner must not be null!");
		
		if (commandExecs.containsKey(runner.getName().toLowerCase(Locale.ENGLISH)))
			log(Level.WARNING, "CommandRunner - "+runner.getName() + " failed to register");
		
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
	
	public abstract PermissionHandler getPermissionHandler();
	
	public abstract Class<? extends ConfigurationSerializable>[] getSerialiables();
}
