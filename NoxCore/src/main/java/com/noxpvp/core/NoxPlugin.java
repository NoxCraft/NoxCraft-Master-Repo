package com.noxpvp.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.permissions.NoxPermission;

public abstract class NoxPlugin extends PluginBase {

	protected Map<String, CommandRunner> commandExecs;
	
	public NoxPlugin() {
		commandExecs = new HashMap<String, CommandRunner>();
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
