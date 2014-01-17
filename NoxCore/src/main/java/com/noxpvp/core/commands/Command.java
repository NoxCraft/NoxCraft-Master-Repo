package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

public interface Command {
	public String getName();
	
	public boolean isRoot();
	
	public String[] getFlags();
	
	public boolean hasArgumentLimit();
	
	public int getMaxArguments();
	
	public String[] getHelp();
	
	public boolean isPlayerOnly();
	
	public Command getParent();
	
	public Command getRoot();
	
	public boolean executeCommand(CommandContext context) throws NoPermissionException;

	public void displayHelp(CommandSender sender);
}
