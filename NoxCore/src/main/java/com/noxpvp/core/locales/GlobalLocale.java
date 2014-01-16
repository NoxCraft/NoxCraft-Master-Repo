package com.noxpvp.core.locales;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.localization.LocalizationEnum;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.MessageUtil;

public class GlobalLocale extends LocalizationEnum {
	public static GlobalLocale HELP_HEADER;
	public static GlobalLocale FAILED_PERMISSION;
	public static GlobalLocale FAILED_PERMISSION_VERBOSE;
	public static GlobalLocale COMMAND_SUCCESS;
	public static GlobalLocale COMMAND_FAILED;
	public static GlobalLocale ERROR_NULL;
	public static GlobalLocale CONSOLE_NEEDPLAYER;
	public static GlobalLocale CONSOLE_ONLYPLAYER;
	
	static {
		//Help Dialogs
		HELP_HEADER = new GlobalLocale("help.header", 
				  "&6-=[&1Nox &l:&4 %0%&6]=-\n"
				+ "&6/&1%1% &d-h&6 > Displays this message");
		
		//Permissions
		FAILED_PERMISSION = new GlobalLocale("permission.denied.default", "&4Permission Denied&r:&e %0%");
		FAILED_PERMISSION_VERBOSE = new GlobalLocale("permission.denied.verbose", FAILED_PERMISSION.getDefault().replace("%0", "%0% >> Node '%1%'"));
		
		//Commands
		COMMAND_SUCCESS = new GlobalLocale("command.successful", "&2Successfully executed command: %0%");
		COMMAND_FAILED = new GlobalLocale("command.failed", "&4Failed to execute command: %0%");
		
		//ERRORS
		ERROR_NULL = new GlobalLocale("error.null", "&4A null pointer error occured: &c%0%");
		
		//CONSOLE
		CONSOLE_NEEDPLAYER = new GlobalLocale("console.needplayer", "This command requires a player: %0%");
		CONSOLE_ONLYPLAYER = new GlobalLocale("console.onlyplayer", "This command can only be run by a player.");
	}
	
	public GlobalLocale(String name, String def) {
		super(name, def);
	}
	
	@Override
	public String get(String... args) {
		return MessageUtil.parseColor(NoxCore.getInstance().getGlobalLocale(getName(), args));
	}
	
	@Override
	public void message(CommandSender sender, String... arguments) {
		MessageUtil.sendMessage(sender, MessageUtil.parseArguments(get(), arguments).split("\n"));
	}
}
