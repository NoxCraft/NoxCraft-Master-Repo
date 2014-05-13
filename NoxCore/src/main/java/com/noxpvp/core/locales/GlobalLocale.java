package com.noxpvp.core.locales;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.utils.gui.MessageUtil;

public class GlobalLocale extends NoxLocale {
	public GlobalLocale(String name, String def) {
		super(name, def);
	}
	
	@Override
	public String get(String... args) {
		return MessageUtil.parseColor(NoxCore.getInstance().getGlobalLocale(getName(), args));
	}
	
	public static GlobalLocale HELP_HEADER;
	/**
	 * Argument 1: Plugin Name<br>
	 * Argument 2: Command Name (Context label preferred)
	 */
	public static GlobalLocale COMMAND_HELP_HEADER;
	public static GlobalLocale HEADER_CLOSE;
	
	public static GlobalLocale FAILED_PERMISSION;
	public static GlobalLocale FAILED_PERMISSION_VERBOSE;
	
	public static GlobalLocale COMMAND_SUCCESS;
	public static GlobalLocale COMMAND_FAILED;
	
	public static GlobalLocale ERROR_NULL;
	
	public static GlobalLocale CONSOLE_NEEDPLAYER;
	public static GlobalLocale CONSOLE_ONLYPLAYER;
	
	public static GlobalLocale CHAT_COLOR_GLOBAL;
	
	static {
		//Help Dialogs
		HELP_HEADER = new GlobalLocale("help.header", "&6[======~*~======([&c%0%&6])======~*~======]");
		COMMAND_HELP_HEADER = new GlobalLocale("help.command-header", "&6[======~*~======([&c%0% - %1%&6])======~*~======]");
		HEADER_CLOSE = new GlobalLocale("heap.header.close", "&6[======~*~========~.*.~========~*~======]");
		
		//Permissions
		FAILED_PERMISSION = new GlobalLocale("permission.denied.default", "&cPermission Denied&r:&e %0%");
		FAILED_PERMISSION_VERBOSE = new GlobalLocale("permission.denied.verbose", FAILED_PERMISSION.getDefault().replace("%0", "%0% >> Node '%1%'"));
		
		//Commands
		COMMAND_SUCCESS = new GlobalLocale("command.successful", "&aSuccessfully executed command: %0%");
		COMMAND_FAILED = new GlobalLocale("command.failed", "&cFailed to execute command: %0%");
		
		//ERRORS
		ERROR_NULL = new GlobalLocale("error.null", "&cA null pointer error occured: &c%0%");
		
		//CONSOLE
		CONSOLE_NEEDPLAYER = new GlobalLocale("console.needplayer", "This command requires a player: %0%");
		CONSOLE_ONLYPLAYER = new GlobalLocale("console.onlyplayer", "This command can only be run by a player.");
		
		//CHAT
		CHAT_COLOR_GLOBAL = new GlobalLocale("chat.color.global", "&b");
		
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}
}
