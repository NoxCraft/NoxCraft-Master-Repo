package com.noxpvp.homes.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.commands.DescriptiveCommandRunner;

public class HomeAdminCommand implements CommandRunner {
	public final static String COMMAND_NAME = "homeadmin";
	
	private Map<String, DescriptiveCommandRunner> subCommands = new HashMap<String, DescriptiveCommandRunner>();;
	
	public HomeAdminCommand()
	{
		addSubCommands(
				new HomeAdminImportCommand()
		);
	}
	
	public String getName() {
		return COMMAND_NAME;
	}

	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args) {
		if (args.length > 0)
		{
			if (isSubCommand(args[0]))
			{
				String[] newArgs = new String[args.length-1];
				System.arraycopy(args, 1, newArgs, 0, newArgs.length);
				if (!subCommands.get(args[0]).execute(sender, flags, newArgs))
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
		for(String line : getHelp())
			sender.sendMessage(line);
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
		mb.yellow("[").blue("NoxHomes Help").yellow("]").newLine();
		mb.setIndent(1);
		for (Entry<String, DescriptiveCommandRunner> entry : subCommands.entrySet())
		{
			boolean first = true;
			for (String line : entry.getValue().getHelp())
				if (!first)
					mb.append(line);
				else
					mb.newLine().append(line);
		}
		mb.setIndent(0);
		
		return mb.lines();
	}
}
