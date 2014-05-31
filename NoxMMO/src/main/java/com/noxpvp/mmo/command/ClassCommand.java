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

package com.noxpvp.mmo.command;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.collections.StringMap;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.command.subcommands.ClassInfoCommand;
import com.noxpvp.mmo.command.subcommands.ClassSwitchCommand;

public class ClassCommand extends BaseCommand {
	public static final String COMMAND_NAME = "class";
	
	public ClassCommand() {
		super(COMMAND_NAME, false);
		
		registerSubCommands(
				new ClassSwitchCommand(),
				new ClassInfoCommand()
				);
	}

	public String[] getFlags() {
		return new String[] {"h", "help"};
	}

	public String[] getHelp() {
		StringMap<BaseCommand> cmds = getSubCommandMap();
		
		MessageBuilder mb = new MessageBuilder();
		
		for (String cmd : cmds.keySet())
			mb.newLine().aqua("/").yellow("class" + " " + cmd);
		
		return mb.lines();
	}

	public int getMaxArguments() {
		return -1;
	}

	@Override
	public CommandResult execute(CommandContext context) throws NoPermissionException {
		return new CommandResult(this, false);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
}
