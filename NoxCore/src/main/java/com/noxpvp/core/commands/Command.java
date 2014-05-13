package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

import com.noxpvp.core.utils.NoxMessageBuilder;

public interface Command {
	
	public void displayHelp(CommandSender sender);
	
	public NoxMessageBuilder onDisplayHelp(NoxMessageBuilder message);
	
	public CommandResult executeCommand(CommandContext context) throws NoPermissionException;
	
	public String[] getFlags();
	
	public String[] getHelp();
	
	public int getMaxArguments();
	
	public String getName();
	
	public Command getParent();
	
	public Command getRoot();
	
	public boolean hasArgumentLimit();
	
	public boolean isPlayerOnly();

	public boolean isRoot();
	
	public static class CommandResult {
		public final boolean success;
		public final BaseCommand executer;
		public final String[] extraMessages;
		
		public CommandResult(BaseCommand executer, boolean success, String... msgs) {
			this.executer = executer;
			this.success = success;
			this.extraMessages = msgs;
		}
	}
}
