package com.noxpvp.core.utils;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.internal.CommonPlugin;
import com.noxpvp.core.NoxPlugin;

public class PermissionHandler {
	private final com.bergerkiller.bukkit.common.internal.PermissionHandler permHandler;
	private NoxPlugin plugin;
	
	public PermissionHandler(NoxPlugin plugin)
	{
		this.plugin = plugin;
		permHandler = CommonPlugin.getInstance().getPermissionHandler();
	}
	
	public boolean hasPermission(CommandSender sender, boolean logConsole, String permission)
	{
		boolean isTrue = permHandler.handlePermission(sender, permission);
		
		if (logConsole && !isTrue)
			plugin.log(Level.WARNING, new MessageBuilder().red("Failed permission from \"").yellow(sender.getName()).red("\" for permission \"").yellow(permission).red("\"").toString());
		
		return isTrue;
	}
	
	public boolean hasPermission(CommandSender sender, String permission)
	{
		return hasPermission(sender, defaultLogToConsole,  permission);
	}
	
	public static boolean defaultLogToConsole = true;

}
