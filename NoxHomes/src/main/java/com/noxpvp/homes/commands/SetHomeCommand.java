package com.noxpvp.homes.commands;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.internal.CommonPlugin;
import com.bergerkiller.bukkit.common.internal.PermissionHandler;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.homes.HomeManager;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;

public class SetHomeCommand implements CommandRunner {
	public final static String COMMAND_NAME = "sethome";
	public final static String PERM_NODE = "sethome";
	
	private HomeManager manager;
	private PermissionHandler permHandler;
	private NoxHomes plugin;
	
	public SetHomeCommand()
	{
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = CommonPlugin.getInstance().getPermissionHandler();
	}
	
	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.yellow("[").aqua("NoxHomes sethome command").yellow("]").newLine();
		mb.blue("/").append(COMMAND_NAME).yellow(" [").aqua("name").yellow("]").newLine();
		mb.aqua("Flags: ").yellow("p|player").aqua(" specifies remote player.");
		
		return mb.lines();
	}
	
	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args) {
		if (!(sender instanceof Player))
		{
			sender.sendMessage(StringUtil.ampToColor(plugin.getLocale("console.onlyplayer")));
			return true;
		}
		
		if ((args.length > 0 && args[0].equalsIgnoreCase("help")) || flags.containsKey("h") || flags.containsKey("help"))
			displayHelp(sender);
		
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
		
		Player player = (Player) sender;
		
		if (own)
			playerName = player.getName();
		else
			playerName = playerFlag;
		
		if (args.length > 0)
			homeName = args[0];
		
		String perm = StringUtil.combine(".", NoxHomes.HOMES_NODE, PERM_NODE, (own? ".": "others.") + (homeName==null ? "default": "named"));
		if (!permHandler.hasPermission(sender, perm))
		{
			sender.sendMessage(plugin.getLocale("permission.denied", "Teleport to homes.", perm));
			return true;
		}
		BaseHome newHome = null;
		if (homeName == null)
			newHome = new DefaultHome(playerName, player);
		else
			newHome = new NamedHome(playerName, homeName, player);
		
//		if (own)
//		{
//			//TODO: Write home limit code.
//		}
		
		
		manager.addHome(newHome);

//		sender.sendMessage(plugin.getLocale("command.failed", "Failed to create new home."));
//		else {
			SafeLocation l = new SafeLocation(newHome.getLocation());
			
			sender.sendMessage(plugin.getLocale("homes.sethome"+(own?".own":""), playerName, homeName, String.format(
					"x=%1 y=%2 z=%3 on world %4", l.getX(), l.getY(), l.getZ(), l.getWorldName()
					)));
//		}
			
		
			
		
		return true;
	}
	
	public void displayHelp(CommandSender sender)
	{
		for (String line : getHelp())
			sender.sendMessage(line);
	}

	public String getName() {
		return COMMAND_NAME;
	}

}