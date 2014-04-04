package com.noxpvp.core.commands;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.NoxCore;

public class CoreCommand extends BaseCommand {
	public final static String COMMAND_NAME = "core";
	public CoreCommand() {
		super(COMMAND_NAME, false);
		registerSubCommand(new UpgradeCommand());
	}
	
	public CommandResult execute(CommandContext context) {
		CommandSender sender = context.getSender();
		String m = new StringBuilder().append(ChatColor.RED).append("Command is not implemented yet..").toString();
		if (!(sender instanceof Player))
			sender.sendMessage(ChatColor.stripColor(m));
		else
			sender.sendMessage(m);
		return new CommandResult(this, true);
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
