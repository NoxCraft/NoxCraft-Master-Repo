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

import java.util.Map.Entry;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.NoxCore;

public class NoxCommand extends BaseCommand {
	public final static String COMMAND_NAME = "nox";
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
	
	public int getMaxArguments() {
		return -1;
	}

	@Override
	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}
}
