package com.noxpvp.homes.commands;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.utils.NoxMessageBuilder;
import com.noxpvp.homes.NoxHomes;

public class HomeAdminCommand extends BaseCommand {
	public static final String COMMAND_NAME = "noxhomes";

	public HomeAdminCommand() {
		super(COMMAND_NAME, false);
		registerSubCommands(
				new HomeAdminImportCommand(),
				new HomeAdminWipeCommand()
		);
	}

	public CommandResult execute(CommandContext context) {
		return new CommandResult(this, false);
	}

	@Override
	public NoxMessageBuilder onDisplayHelp(NoxMessageBuilder message) {
		for (BaseCommand cmd : getSubCommandMap().values())
			message.withCommand(cmd, true);

		return message;
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
