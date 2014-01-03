package com.noxpvp.homes.commands;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.internal.CommonPlugin;
import com.bergerkiller.bukkit.common.internal.PermissionHandler;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.homes.HomeManager;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.tp.BaseHome;

public class DeleteHomeCommand implements CommandRunner {
	public static final String COMMAND_NAME = "delhome";
	public static final String PERM_NODE = "delhome";
	private HomeManager manager;
	private PermissionHandler permHandler;
	private NoxHomes plugin;
	
	public DeleteHomeCommand()
	{
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = CommonPlugin.getInstance().getPermissionHandler();
	}
	
	
	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args) {
		if ((args.length > 0 && args[0].equalsIgnoreCase("help")) || flags.containsKey("h") || flags.containsKey("help"))
		{
			displayHelp(sender);
			return true;
		}
		
		boolean own;
		String playerFlag = null;
		if (flags.containsKey("p"))
			playerFlag = flags.get("p").toString();
		else if (flags.containsKey("player"))
			playerFlag = flags.get("player").toString();
		
		if (playerFlag == null)
			own = true;
		else
			own = playerFlag.equals(sender.getName()) && sender instanceof Player;
		
		String homeName = null;
		
		String playerName = null;
		if (own && sender instanceof Player)
			playerName = sender.getName();
		else if (!own)
			playerName = playerFlag;
		else
		{
			sender.sendMessage(StringUtil.ampToColor(plugin.getGlobalLocale("console.needplayer", "To delete a home.")));
			return true;
		}
		
		if (args.length > 0)
			homeName = args[0];
		
		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, (own? ".": "others.") + (homeName==null ? "default": "named"));
		
		if (!permHandler.hasPermission(sender, perm))
		{
			sender.sendMessage(plugin.getGlobalLocale("permission.denied", "Delete homes", perm));
			return true;
		}
		
		BaseHome home = manager.getHome(playerName, homeName);
		
		manager.removeHome(home);
		sender.sendMessage(StringUtil.ampToColor(plugin.getLocale("homes.delhome"+((own)?".own":""), playerName, homeName)));
		
		return true;
	}
	
	public void displayHelp(CommandSender sender)
	{
		for (String line : getHelp())
			sender.sendMessage(line);
	}
	
	public String[] getHelp()
	{
		MessageBuilder mb = new MessageBuilder();
		mb.yellow("[").aqua("NoxHomes Delete Home Command").yellow("]").newLine();
		mb.blue("/").append(COMMAND_NAME).yellow(" [").aqua("name").yellow("]").newLine();
		mb.aqua("Flags: ").yellow("p|player").aqua(" specifies remote player to delete home");
		return mb.lines();
	}

	public String getName() {
		return COMMAND_NAME;
	}

}
