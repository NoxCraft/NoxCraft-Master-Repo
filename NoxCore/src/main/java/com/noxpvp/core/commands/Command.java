package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

public interface Command {
	public void displayHelp(CommandSender sender);
	
	public boolean executeCommand(CommandContext context) throws NoPermissionException;
	
	public String[] getFlags();
	
	public String[] getHelp();
	
	public int getMaxArguments();
	
	public String getName();
	
	public Command getParent();
	
	public Command getRoot();
	
	public boolean hasArgumentLimit();
	
	public boolean isPlayerOnly();

	public boolean isRoot();
}
