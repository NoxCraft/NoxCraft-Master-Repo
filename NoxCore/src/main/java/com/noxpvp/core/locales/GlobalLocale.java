package com.noxpvp.core.locales;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.localization.LocalizationEnum;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.MessageUtil;

public class GlobalLocale extends LocalizationEnum {
	
	//Permissions
	public final static GlobalLocale FAILED_PERMISSION = new GlobalLocale("permission.denied.default", "&4Permission Denied&r:&e %0%");
	public final static GlobalLocale FAILED_PERMISSION_VERBOSE = new GlobalLocale("permission.denied.verbose", FAILED_PERMISSION.getDefault().replace("%0", "%0% >> Node '%1%'"));
	
	//Commands
	public final static GlobalLocale COMMAND_SUCCESS = new GlobalLocale("command.successful", "&2Successfully executed command: %0%");
	public final static GlobalLocale COMMAND_FAILED = new GlobalLocale("command.failed", "&4Failed to execute command: %0%");
	
	//ERRORS
	public final static GlobalLocale ERROR_NULL = new GlobalLocale("error.null", "&4A null pointer error occured: &c%0%");
	
	//CONSOLE
	public final static GlobalLocale CONSOLE_NEEDPLAYER = new GlobalLocale("console.needplayer", "This command requires a player: %0%");
	public final static GlobalLocale CONSOLE_ONLYPLAYER = new GlobalLocale("console.onlyplayer", "This command can only be run by a player.");
	
	
	public GlobalLocale(String name, String def) {
		super(name, def);
	}
	
	@Override
	public String get(String... args) {
		return NoxCore.getInstance().getGlobalLocale(getName(), args);
	}
	
	@Override
	public void message(CommandSender sender, String... arguments) {
		MessageUtil.sendMessage(sender, MessageUtil.parseArguments(MessageUtil.parseColor(get()), arguments).split("\n"));
	}
}
