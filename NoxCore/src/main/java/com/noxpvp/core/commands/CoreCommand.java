package com.noxpvp.core.commands;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreCommand implements CommandRunner {
	public final static String COMMAND_NAME = "noxcore";
	
	public String getName() {
		return COMMAND_NAME;
	}
	
	public String[] getHelp() {
		return new String[0];
	}

	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args) {
		String m = new StringBuilder().append(ChatColor.RED).append("Command is not implemented yet..").toString();
		if (!(sender instanceof Player))
			sender.sendMessage(ChatColor.stripColor(m));
		else
			sender.sendMessage(m);
		return false;
	}

	public void displayHelp(CommandSender sender) {
		for (String line : getHelp())
			sender.sendMessage(line);
	}

}
