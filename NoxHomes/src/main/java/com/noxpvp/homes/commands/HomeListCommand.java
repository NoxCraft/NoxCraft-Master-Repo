package com.noxpvp.homes.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.homes.PlayerManager;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.tp.BaseHome;

public class HomeListCommand extends BaseCommand {
	public static final String COMMAND_NAME = "homes";
	private final PermissionHandler permHandler;
	private NoxHomes plugin;
	public static final String LIST_PERM_NODE = "list";
	private PlayerManager manager;
	
	public HomeListCommand()
	{
		super(COMMAND_NAME, false);
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}
	
	public CommandResult execute(CommandContext context) {
		if (context.hasFlag("h") || context.hasFlag("help"))
			return new CommandResult(this, false);
		
		CommandSender sender = context.getSender();
		
		if (manager == null)
		{
			manager = plugin.getHomeManager();
			if (manager == null);
			{
				return new CommandResult(this, true);
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
			return new CommandResult(this, true);
		} else if ((player == null || player.length() == 0)) {
			MessageUtil.sendLocale(sender, GlobalLocale.CONSOLE_NEEDPLAYER, "Use the -p \"PlayerName\" flag");
			return new CommandResult(this, true);
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
		return new CommandResult(this, true);
	}
	
	public String[] getHelp()
	{
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).newLine();
		
		return mb.lines();
	}

	public String[] getFlags() {
		return new String[]{"h", "help", "p", "player"};
	}

	public int getMaxArguments() {
		return 0;
	}

	public NoxPlugin getPlugin() {
		return plugin;
	}
}
