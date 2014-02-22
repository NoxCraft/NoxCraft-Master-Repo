package com.noxpvp.mmo.command.subcommands;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.gui.ClassMenu;

public class ClassInfoCommand extends BaseCommand {

	public static final String COMMAND_NAME = "info";

	public ClassInfoCommand() {
		super(COMMAND_NAME, false);
	}
	
	public String[] getFlags() {
		return new String[] {};
	}

	public String[] getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxArguments() {
		return 2;
	}

	@Override
	public boolean execute(CommandContext context) throws NoPermissionException {
		return false;
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
