package com.noxpvp.homes.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;

public class HomeCommand implements CommandRunner {
	public static final String COMMAND_NAME = "home";
	public static final String PERM_NODE = "home";
	private HomeManager manager;
	private NoxHomes plugin;
	private final PermissionHandler permHandler;;
	
	public HomeCommand()
	{
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}
	
	public boolean execute(CommandContext context) {
		
		if (!context.isPlayer()) {
			MessageUtil.sendLocale(context.getSender(), GlobalLocale.CONSOLE_ONLYPLAYER);
			return true;
		}
		Player sender = context.getPlayer();
		
		if (context.hasFlag("h") || context.hasFlag("help"))
			return false; //Its caught anyway why repeat display help code...

		String player = null, homeName = null;

		if (context.hasFlag("p"))
			player = context.getFlag("p", String.class);
		else if (context.hasFlag("player"))
			player = context.getFlag("player", String.class);
		else
			player = context.getPlayer().getName(); //Impossible to NPE. If it does. We got problems..
		
		if (context.hasArgument(0))
			homeName = context.getArgument(0);
		
		boolean own = sender.getName().equals(player);
		
		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, (own? "": "others.") + (homeName==null ? DefaultHome.PERM_NODE: NamedHome.PERM_NODE));
		if (!permHandler.hasPermission(sender, perm))
			throw new NoPermissionException(sender, perm, new StringBuilder().append("Teleport to ").append((own?"your ":"others ")).append("homes.").toString());
		
		BaseHome home = manager.getHome(player, homeName);
		if (home == null) 
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "The home \"" + (homeName == null? "default": homeName) + "\" does not exist");
		else if (home.tryTeleport(sender))
			MessageUtil.sendLocale(plugin, sender, "homes.home"+ (own?".own":""), player, (homeName == null? "default": homeName));
		else
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Could not teleport home.");
		
		return true;
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

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).aqua(" [").aqua("name]").newLine();
		return mb.lines();
	}

}
