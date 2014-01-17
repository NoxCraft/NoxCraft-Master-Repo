package com.noxpvp.homes.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.SafeLocation;
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

public class SetHomeCommand implements CommandRunner {
	public static final String COMMAND_NAME = "sethome";
	public static final String PERM_NODE = "sethome";
	
	private HomeManager manager;
	private final PermissionHandler permHandler;
	private NoxHomes plugin;
	
	public SetHomeCommand()
	{
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}
	
	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).aqua(" [name]").newLine();
		
		return mb.lines();
	}
	
	public boolean execute(CommandContext context) {
		if (!context.isPlayer())
		{
			MessageUtil.sendLocale(context.getSender(), GlobalLocale.CONSOLE_ONLYPLAYER);
			return true;
		}
		
		Player sender = context.getPlayer();
		
		if (context.hasFlag("h") || context.hasFlag("help"))
			return false;
		
		String player = null;
		
		if (context.hasFlag("p"))
			player = context.getFlag("p", String.class);
		else if (context.hasFlag("player"))
			player = context.getFlag("player", String.class);
		else
			player = sender.getName();
		
		boolean own = player.equals(sender.getName());
		String homeName = null;
		
		if (context.hasArgument(0))
			homeName = context.getArgument(0);
		
		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, (own? "": "others.") + (homeName==null ? "default": "named"));
		if (!permHandler.hasPermission(sender, perm))
			throw new NoPermissionException(sender, perm, new StringBuilder("Set New Homes for ").append((own?"self":"others")).append(".").toString());

		
		boolean success = false;
		BaseHome newHome = null;
		if (homeName == null)
			newHome = new DefaultHome(player, sender);
		else
			newHome = new NamedHome(player, homeName, sender);
		
		if (newHome instanceof DefaultHome)
			homeName = DefaultHome.PERM_NODE;
		boolean good = false;
		if (own)
		{
			if (plugin.getLimitsManager().canAddHome(player))
			{
				good = true;
			} else {
				if ( manager.getHome(player, homeName) != null) {
					good = true;
					manager.removeHome(plugin.getHomeManager().getHome(player, homeName));
				} else {
					MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "You already " + plugin.getHomeManager().getHomes(player).size() + "/"+ plugin.getLimitsManager().getLimit(player) + " of the maximum amount of homes allowed.");
					return true;
				}
			}
		}
		else
			good = true;
		
		if (good) {
			manager.addHome(newHome);
			success = manager.getHome(player, homeName) != null;
		} else
			success = false;
		if (success) {
			SafeLocation l = new SafeLocation(newHome.getLocation());
			MessageUtil.sendLocale(plugin, sender, "homes.sethome"+(own?".own":""), player, (homeName == null? "default": homeName), String.format(
				"x=%1$.1f y=%2$.1f z=%3$.1f on world \"%4$s\"", l.getX(), l.getY(), l.getZ(), l.getWorldName()
				));
			
			manager.save();
		} else {
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Home never stored in memory...");
		}
		
		return true;
	}
	
	public void displayHelp(CommandSender sender)
	{
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
