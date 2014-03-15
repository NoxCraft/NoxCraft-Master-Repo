package com.noxpvp.mmo.command;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.collections.StringMap;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.locales.GlobalLocale;
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
			mb.newLine().aqua("/").yellow("class" + cmd);
		
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
