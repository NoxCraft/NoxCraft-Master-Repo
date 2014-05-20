package com.noxpvp.core.commands;

import java.util.Map.Entry;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.NoxMessageBuilder;

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
