package com.noxpvp.homes.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.core.utils.chat.MessageUtil;
import com.noxpvp.homes.PlayerManager;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;

public class SetHomeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "sethome";
	public static final String PERM_NODE = "sethome";
	
	private PlayerManager manager;
	private final PermissionHandler permHandler;
	private NoxHomes plugin;
	
	public SetHomeCommand()
	{
		super(COMMAND_NAME, true);
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}
	
	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).aqua(" [name]").newLine();
		
		return mb.lines();
	}
	
	public CommandResult execute(CommandContext context) {
		if (!context.isPlayer())
		{
			MessageUtil.sendLocale(context.getSender(), GlobalLocale.CONSOLE_ONLYPLAYER);
			return new CommandResult(this, true);
		}
		
		Player sender = context.getPlayer();
		
		if (context.hasFlag("h") || context.hasFlag("help"))
			return new CommandResult(this, false);
		
		/*
		 * Scoped for variable usage.
		 */
		{
			MessageBuilder mb = new MessageBuilder().red("You are not allowed to set home in other towns.");
			Location loc = sender.getLocation();
			String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, "other-towns");
			if (TownyUtil.isClaimedLand(loc) && !TownyUtil.isOwnLand(sender, loc) && permHandler.hasPermission(sender, perm))
				return new CommandResult(this, true, mb.lines());
		}
		
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
					return new CommandResult(this, true);
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
		} else {
			MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Home never stored in memory...");
		}
		
		return new CommandResult(this, true);
	}

	public String[] getFlags() {
		return new String[]{"h", "help", "p", "player"};
	}

	public int getMaxArguments() {
		return 1;
	}

	public NoxPlugin getPlugin() {
		return plugin;
	}
}
