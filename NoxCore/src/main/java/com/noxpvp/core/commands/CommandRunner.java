package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

public interface CommandRunner {
	public String getName();
	public String[] getHelp();
	public boolean execute(CommandContext context);
	public void displayHelp(CommandSender sender);
}
