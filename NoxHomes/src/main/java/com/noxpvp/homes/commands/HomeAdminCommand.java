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

	public CommandResult execute(CommandContext context) {
		return new CommandResult(this, false);
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
