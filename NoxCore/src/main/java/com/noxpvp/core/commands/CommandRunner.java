package com.noxpvp.core.commands;

import java.util.Map;

import org.bukkit.command.CommandSender;

public interface CommandRunner {
	public String getName();
	public String[] getHelp();
	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args);
	public void displayHelp(CommandSender sender);
}
