package com.noxpvp.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.utils.CommandUtil;

public abstract class NoxPlugin extends PluginBase {

	protected Map<String, CommandRunner> commandExecs = new HashMap<String, CommandRunner>();;
	
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
		if (commandExecs.containsKey(runner.getName().toLowerCase()))
			this.getLogger().warning("CommandRunner - "+runner.getName() + " failed to register");
		
		commandExecs.put(runner.getName().toLowerCase(), runner);
		try {
			Bukkit.getPluginCommand(runner.getName().toLowerCase()).setExecutor(this);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	protected void addPermissions(NoxPermission... perms)
	{
		NoxCore.getInstance().addPermissions(perms);
	}
	
	protected void addPermission(NoxPermission perm) {
		NoxCore.getInstance().addPermission(perm);
	}
	
	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}
	
}
