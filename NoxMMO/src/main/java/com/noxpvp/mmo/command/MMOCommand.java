package com.noxpvp.mmo.command;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.gui.NoxMMOMenu;

public class MMOCommand extends BaseCommand {

	public static final String COMMAND_NAME = "mmo";
	
	public MMOCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return new String[] {};
	}

	public int getMaxArguments() {
		return 0;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		new NoxMMOMenu(context.getPlayer()).show();
	
		return new CommandResult(this, true);
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}

}
