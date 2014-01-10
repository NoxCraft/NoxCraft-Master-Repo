package com.noxpvp.homes.commands;

import org.bukkit.command.CommandSender;

import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.utils.MessageUtil;


public class LocateHomeCommand implements CommandRunner {
	public static final String COMMAND_NAME = "locatehome";
	
	public String getName() {
		return COMMAND_NAME;
	}

	public String[] getHelp() {
		return new String[] {"THIS COMMAND IS NOT IMPLEMENTED" };
	}
	
	public boolean execute(CommandContext context) {
		displayHelp(context.getSender());
		return false;
	}

	public void displayHelp(CommandSender sender) {
		MessageUtil.sendMessage(sender, getHelp());
	}

}
