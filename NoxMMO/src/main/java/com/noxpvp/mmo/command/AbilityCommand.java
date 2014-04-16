package com.noxpvp.mmo.command;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;

public class AbilityCommand extends BaseCommand {

	public static final String COMMAND_NAME = "ability";
	
	public AbilityCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return new String[] { "h", "help" };
	}

	public String[] getHelp() {
		return new String[]{"/ability <ability name>"};
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
