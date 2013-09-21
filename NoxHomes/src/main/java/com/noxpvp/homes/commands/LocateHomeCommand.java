package com.noxpvp.homes.commands;

import java.util.Map;

import org.bukkit.command.CommandSender;

import com.noxpvp.core.commands.CommandRunner;


public class LocateHomeCommand implements CommandRunner {
	public final static String COMMAND_NAME = "locatehome";
	
	public String getName() {
		return COMMAND_NAME;
	}

	public String[] getHelp() {
		return new String[0];
	}
	
	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	public void displayHelp(CommandSender sender) {
		for (String line : getHelp())
			sender.sendMessage(line);
	}

}
