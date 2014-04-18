package com.noxpvp.mmo.command.subcommands;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;

public class AbilityInfoCommand extends BaseCommand {

	public static final String COMMAND_NAME = "info";

	public AbilityInfoCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return new String[] { "h", "help" };
	}

	public String[] getHelp() {
		return null;
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
