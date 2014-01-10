package com.noxpvp.homes.commands;

import java.util.Random;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.commands.DescriptiveCommandRunner;
import com.noxpvp.core.commands.ICommandContext;
import com.noxpvp.core.utils.MessageUtil;
import com.noxpvp.homes.HomeManager;
import com.noxpvp.homes.NoxHomes;

public class HomeAdminWipeCommand implements DescriptiveCommandRunner {
	public static final String COMMAND_NAME = "wipehomes";
	public static final String PERM_NODE = "wipe.homes";
	private static final Random r = new Random();
	private HomeManager manager;
	private NoxHomes plugin;
	
	private String key;
	private String[] helpLines;
	
	public HomeAdminWipeCommand() {
		plugin = NoxHomes.getInstance();
		key = getNextKey();
		
		updateHelp();
		
		manager = plugin.getHomeManager();
	}
	
	public String getName() {
		return COMMAND_NAME;
	}

	public boolean execute(ICommandContext context) {
		String[] args = context.getArguments();
		CommandSender sender = context.getSender();
		
		boolean wiped = false;
		if (args.length < 1)
			return false;
		
		String k = null;
		if (args.length > 0) 
		{
			k = args[0];
		
			if (k.equals(key))
			{
				manager.clear();
				manager.save();
				wiped = true;
			}
		}
		
		if (wiped)
		{
			key = getNextKey();
			MessageUtil.sendGlobalLocale(plugin, sender, "command.successful", "Wiped home data.");
			
			updateHelp();
		} else {
			MessageUtil.sendGlobalLocale(plugin, sender, "command.failed", "Could not wipe data.");
		}
		return true;
	}
	
	private String getNextKey() {
		return String.valueOf(r.nextInt(NoxCore.getInstance().getConfig().getInt("wipe-confirmation-number", 9999)));
	}

	private void updateHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.yellow("[").blue(" Nox Admin Wipe Command ").yellow("]").newLine();
		mb.red("WILL WIPE ALL HOME LOCATIONS ON ALL PLAYERS").newLine();
		mb.blue("/").append(HomeAdminCommand.COMMAND_NAME).append(' ').append(COMMAND_NAME).newLine();
		mb.red("Current Safety Key: ").yellow(key);
		
		helpLines = mb.lines();
	}
	
	public String[] getHelp() {
		return helpLines;
	}

	public String[] getDescription() {
		return new String[]{"Wipes all home data from all players."};
	}

	public String[] getFlags() {
		return new String[0];
	}

	public void displayHelp(CommandSender sender) {
		MessageUtil.sendMessage(sender, getHelp());
	}
	
}
