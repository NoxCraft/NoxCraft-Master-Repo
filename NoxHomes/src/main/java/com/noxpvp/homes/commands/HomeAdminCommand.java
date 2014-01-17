package com.noxpvp.homes.commands;

import java.util.Map.Entry;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.homes.NoxHomes;

public class HomeAdminCommand extends BaseCommand {
	public static final String COMMAND_NAME = "noxhomes";
	
	public HomeAdminCommand()
	{
		super(COMMAND_NAME, false);
		registerSubCommands(
				new HomeAdminImportCommand(),
				new HomeAdminWipeCommand()
		);
	}

	public boolean execute(CommandContext context) {
		displayHelp(context.getSender());
		return true;
	}
	
	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.setIndent(1);
		mb.setSeparator("\n");
		for (Entry<String, BaseCommand> entry : getSubCommandMap().entrySet())
			for (String line : entry.getValue().getHelp())
				mb.append(line);
		mb.setIndent(0);
		
		return mb.lines();
	}

	public String[] getFlags() {
		return new String[]{"h", "help"};
	}

	public int getMaxArguments() {
		return -1;
	}

	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}
}
