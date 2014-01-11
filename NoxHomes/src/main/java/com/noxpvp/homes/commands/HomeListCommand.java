package com.noxpvp.homes.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.utils.MessageUtil;
import com.noxpvp.core.utils.PermissionHandler;
import com.noxpvp.homes.HomeManager;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.tp.BaseHome;

public class HomeListCommand implements CommandRunner {
	public static final String COMMAND_NAME = "homes";
	private final PermissionHandler permHandler;
	private NoxHomes plugin;
	public static final String LIST_PERM_NODE = "list";
	private HomeManager manager;
	
	public HomeListCommand()
	{
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}
	
	public boolean execute(CommandContext context) {
		if (context.hasFlag("h") || context.hasFlag("help"))
			return false;
		
		CommandSender sender = context.getSender();
		
		if (manager == null)
		{
			manager = plugin.getHomeManager();
			if (manager == null);
			{
				return true;
			}
		}
		
		String player = null;
		
		if (context.hasFlag("p"))
			player = context.getFlag("p", String.class);
		else if (context.hasFlag("player"))
			player = context.getFlag("player", String.class);
		else if (context.isPlayer())
			player = context.getPlayer().getName();
		
		if ((player == null || player.length() == 0) && context.isPlayer()) {
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Player match failed. Player was " + ((player == null)? "null": "blank"));
			return true;
		} else if ((player == null || player.length() == 0)) {
			MessageUtil.sendLocale(sender, GlobalLocale.CONSOLE_NEEDPLAYER, "Use the -p \"PlayerName\" flag");
			return true;
		}
		
		boolean own = false;
		if (player.equals(sender.getName()) && context.isPlayer())
			own = true;
		
		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, LIST_PERM_NODE, (own ? "own" : "others"));
		if (!permHandler.hasPermission(sender, perm))
			throw new NoPermissionException(sender, perm, new StringBuilder("Not allowed to view list of ").append((own?"your own ":"other's ")).append("homes!").toString());
		
		List<BaseHome> homes = manager.getHomes(player);
		
		String homelist;
		List<String> homeNames = new ArrayList<String>(homes.size());
		
		
		if (homes.isEmpty())
			homelist = "None";
		else
		{
			for (BaseHome home : homes)
				homeNames.add(home.getName());
			homelist = StringUtil.combineNames(homeNames);
		}
		
		if (own)
			player = "own";
		
		MessageUtil.sendLocale(plugin, sender, "homes.list", player, homelist);//TODO: Prettify large lists.
		return true;
	}
	
	public String[] getHelp()
	{
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).newLine();
		
		return mb.lines();
	}
	
	public void displayHelp(CommandSender sender) {
		MessageBuilder mb = new MessageBuilder();
		
		mb.setSeparator("\n");
		for (String line : GlobalLocale.HELP_HEADER.get("Homes", COMMAND_NAME).split("\n"))
			mb.append(line);
		for (String line : getHelp())
			mb.append(line);
		
		MessageUtil.sendMessage(sender, mb.lines());
	}

	public String getName() {
		return COMMAND_NAME;
	}

}
