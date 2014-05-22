package com.noxpvp.core.commands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.NoxCore;

public class WhoisCommand extends BaseCommand {
	public static final String COMMAND_NAME = "whois";
	
	public static final String PERM_NODE = "whois";
	
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
