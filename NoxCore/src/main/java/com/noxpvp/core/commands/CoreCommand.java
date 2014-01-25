package com.noxpvp.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxCore;

public class CoreCommand extends BaseCommand {
	public final static String COMMAND_NAME = "core";
	public CoreCommand() {
		super(COMMAND_NAME, false);
	}
	
	public boolean execute(CommandContext context) {
		CommandSender sender = context.getSender();
		String m = new StringBuilder().append(ChatColor.RED).append("Command is not implemented yet..").toString();
		if (!(sender instanceof Player))
			sender.sendMessage(ChatColor.stripColor(m));
		else
			sender.sendMessage(m);
		return true;
	}
	
	public String[] getFlags() {
		return new String[0];
	}
	
	public String[] getHelp() {
		return new String[0];
	}
	
	public int getMaxArguments() {
		return 0;
	}

	@Override
	public NoxCore getPlugin() {
		return NoxCore.getInstance();
	}
}
