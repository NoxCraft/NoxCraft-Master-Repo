package com.noxpvp.homes.commands;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.homes.NoxHomes;


public class LocateHomeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "locatehome";
	
	private NoxHomes plugin;
	
	public LocateHomeCommand() {
		super(COMMAND_NAME, false);
		this.plugin = NoxHomes.getInstance();
	}
	
	public String[] getHelp() {
		return new String[] {"THIS COMMAND IS NOT IMPLEMENTED" };
	}
	
	public CommandResult execute(CommandContext context) {
		return new CommandResult(this, false);
	}

	public String[] getFlags() {
		return new String[]{"h", "help", "p", "player"};
	}

	public int getMaxArguments() {
		return 0;
	}

	@Override
	public NoxPlugin getPlugin() {
		return plugin;
	}

}
