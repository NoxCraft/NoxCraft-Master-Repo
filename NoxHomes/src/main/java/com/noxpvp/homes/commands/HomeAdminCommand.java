package com.noxpvp.homes.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.commands.DescriptiveCommandRunner;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.utils.MessageUtil;

public class HomeAdminCommand implements CommandRunner {
	public static final String COMMAND_NAME = "noxhomes";
	
	private Map<String, DescriptiveCommandRunner> subCommands = new HashMap<String, DescriptiveCommandRunner>();;
	
	public HomeAdminCommand()
	{
		addSubCommands(
				new HomeAdminImportCommand(),
				new HomeAdminWipeCommand()
		);
	}
	
	public String getName() {
		return COMMAND_NAME;
	}

	public boolean execute(CommandContext context) {
		String[] args = context.getArguments();
		
		CommandSender sender = context.getSender();
		
		if (args.length > 0)
		{
			if (isSubCommand(args[0]))
			{
				String[] newArgs;
				if (args.length == 1)
					newArgs = new String[0];
				else if (args.length == 2) {
					newArgs = new String[1];
					newArgs[0] = args[1];
				} else
					newArgs = Arrays.copyOfRange(args, 1, args.length-1);
				if (!subCommands.get(args[0]).execute(new CommandContext(sender, context.getFlags(), newArgs)))
					displayHelp(sender);
			} else {
				displayHelp(sender);
			}
			return true;
		} else {
			displayHelp(sender);
			return true;
		}
	}
	
	public void displayHelp(CommandSender sender)
	{
		MessageBuilder mb = new MessageBuilder();
		
		mb.setSeparator("\n");
		for (String line : GlobalLocale.HELP_HEADER.get("Homes", COMMAND_NAME).split("\n"))
			mb.append(line);
		for (String line : getHelp())
			mb.append(line);
		
		MessageUtil.sendMessage(sender, mb.lines());
	}
	
	protected int addSubCommands(DescriptiveCommandRunner... runners)
	{
		int good = runners.length;
		for (DescriptiveCommandRunner runner : runners)
			try {
				addSubCommand(runner);
			} catch (IllegalArgumentException e) {good--;}
		
		return good;
	}
	
	private boolean isSubCommand(String arg)
	{
		return subCommands.containsKey(arg);
	}
	
	protected void addSubCommand(DescriptiveCommandRunner runner)
	{
		Validate.notNull(runner, "Must not be null sub command");
		
		String name = runner.getName();
		if (subCommands.containsKey(name))
			return;
		
		subCommands.put(name, runner);
	}
	
	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.setIndent(1);
		mb.setSeparator("\n");
		for (Entry<String, DescriptiveCommandRunner> entry : subCommands.entrySet())
			for (String line : entry.getValue().getHelp())
				mb.append(line);
		mb.setIndent(0);
		
		return mb.lines();
	}
}
