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

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.NoxCore;

public class WhoisCommand extends BaseCommand {
	public final static String COMMAND_NAME = "whois";
	
	public final static String PERM_NODE = "whois";
	
	public WhoisCommand() {
		super(COMMAND_NAME, false);
	}

	public String[] getFlags() {
		return new String[] {"h", "help"};
	}

	public String[] getHelp() {
		return new MessageBuilder().gold("/").blue(COMMAND_NAME).append(' ').red("<playerName>").lines(); //TODO: Cache this..
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public CommandResult execute(CommandContext context) throws NoPermissionException {
		context.getSender().sendMessage("This command is not implemented.");
		return new CommandResult(this, true);
	}

	@Override
	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}

}
