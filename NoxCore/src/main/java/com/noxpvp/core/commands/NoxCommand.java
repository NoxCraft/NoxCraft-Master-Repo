/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */


package com.noxpvp.core.commands;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.NoxMessageBuilder;

public class NoxCommand extends BaseCommand {
	public static final String COMMAND_NAME = "nox";

	public NoxCommand() {
		super(COMMAND_NAME, false);
		registerSubCommand(new UpgradeCommand());
	}

	public CommandResult execute(CommandContext context) {
		return new CommandResult(this, false);
	}

	public String[] getFlags() {
		return new String[]{"h", "help"};
	}

	@Override
	public NoxMessageBuilder onDisplayHelp(NoxMessageBuilder message) {
		for (BaseCommand cmd : getSubCommandMap().values())
			message.withCommand(cmd, true);

		return message;
	}

	public int getMaxArguments() {
		return -1;
	}

	@Override
	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}
}
