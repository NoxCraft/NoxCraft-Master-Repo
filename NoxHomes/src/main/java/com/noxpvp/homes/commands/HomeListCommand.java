package com.noxpvp.homes.commands;

import java.util.ArrayList;
import java.util.List;
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

public class HomeListCommand implements CommandRunner {
	public final static String COMMAND_NAME = "listhomes";
	private final PermissionHandler permHandler;
	private NoxHomes plugin;
	public final static String LIST_PERM_NODE = "list";
	private HomeManager manager;
	
	public HomeListCommand()
	{
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = CommonPlugin.getInstance().getPermissionHandler();
	}
	
	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args) {
		if ((args.length > 0 && args[0].equalsIgnoreCase("help")) || flags.containsKey("h") || flags.containsKey("help"))
			return false;
		
		if (manager == null)
		{
			manager = plugin.getHomeManager();
			if (manager == null);
			{
				sender.sendMessage(StringUtil.ampToColor(plugin.getGlobalLocale("error.null", "HomeManager reference in Home List Object.")));
				return true;
			}
		}
		
		String player;
		boolean own = false;
		if (flags.containsKey("p") || flags.containsKey("player"))
		{
			if (flags.containsKey("p"))
				player = flags.get("p").toString();
			else
				player = flags.get("player").toString();
			
			if (player == null || player.length() == 0)
			{
				sender.sendMessage(StringUtil.ampToColor(plugin.getGlobalLocale("error.homes.list.others", (player == null)? "null": player)));
				return true;
			}
			
			if ((sender instanceof Player))
				if (((Player)sender).getName().equals(player))
					own = true;
		} else if (sender instanceof Player){
			player = ((Player)sender).getName();
			own = true;
		} else {
			sender.sendMessage(plugin.getGlobalLocale("console.needplayer", "Use the -p \"PlayerName\" flag"));
			return true;
		}
		
		List<BaseHome> homes = manager.getHomes(player);
		
		String homelist;
		List<String> homeNames = new ArrayList<String>(homes.size());
		
		
		if (homes.isEmpty())
			homelist = "None";
		else
		{
			for (BaseHome home : homes)
				homeNames.add(home.getName());
			homelist = StringUtil.combine(",", homeNames);
		}
		
		if (own)
			player = "own";
		
		String perm = StringUtil.combine(".", NoxHomes.HOMES_NODE, LIST_PERM_NODE, (own ? "own" : "others"));
		if (!permHandler.hasPermission(sender, perm))
		{
			sender.sendMessage(StringUtil.ampToColor(plugin.getGlobalLocale("permission.denied", "Can not view other people's Homes!", perm)));
			return true;
		}
		sender.sendMessage(plugin.getLocale("homes.list", player, homelist));//TODO: Prettify large lists.
		return true;
	}
	
	public String[] getHelp()
	{
		MessageBuilder mb = new MessageBuilder();
		mb.yellow("[").aqua("NoxHomes list homes command").yellow("]").newLine();
		mb.blue("/").append(COMMAND_NAME).newLine();
		mb.aqua("Flags: ").yellow("p|player").aqua(" defines remote Player");
		
		return mb.lines();
	}
	
	public void displayHelp(CommandSender sender) {
		for (String line : getHelp())
			sender.sendMessage(line);
	}

	public String getName() {
		return COMMAND_NAME;
	}

}
