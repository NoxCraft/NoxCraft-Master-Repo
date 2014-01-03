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

public class HomeCommand implements CommandRunner {
	public static final String COMMAND_NAME = "home";
	public static final String PERM_NODE = "home";
	private HomeManager manager;
	private NoxHomes plugin;
	private PermissionHandler permHandler;
	
	public HomeCommand()
	{
		plugin = NoxHomes.getInstance();
		permHandler = CommonPlugin.getInstance().getPermissionHandler();
	}
	
	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args) {
		if (!(sender instanceof Player))
		{
			sender.sendMessage(StringUtil.ampToColor(plugin.getGlobalLocale("console.onlyplayer")));
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
		
		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, (own? ".": "others.") + (homeName==null ? "default": "named"));
		if (!permHandler.hasPermission(sender, perm))
		{
			sender.sendMessage(plugin.getGlobalLocale("permission.denied", "Teleport to homes.", perm));
			return true;
		}
		
		BaseHome home = manager.getHome(playerName, homeName);
		
		if (home.tryTeleport(player))
			player.sendMessage(StringUtil.ampToColor(plugin.getLocale("homes.home"+ (own?".own":""), playerName, homeName)));
		else
			player.sendMessage(StringUtil.ampToColor(plugin.getGlobalLocale("commands.failed", "Could not teleport home.")));
		
		return true;
	}

	public void displayHelp(CommandSender sender) {
		for (String line : getHelp())
			sender.sendMessage(line);
	}

	public String getName() {
		return COMMAND_NAME;
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.yellow("[").aqua("NoxHomes Home Command").yellow("]").newLine();
		mb.blue("/").append(COMMAND_NAME).yellow(" [").aqua("name").yellow("]").newLine();
		mb.aqua("Flags: ").yellow("p|player ").aqua("Remote player. Use the home as if that player.");
		
		return mb.lines();
	}

}
