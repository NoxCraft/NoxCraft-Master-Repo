package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.MasterReloader;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.reloader.Reloader;
import com.noxpvp.core.utils.MessageUtil;
import com.noxpvp.core.utils.PermissionHandler;

public class ReloadCommand implements DescriptiveCommandRunner {
	public static final String COMMAND_NAME = "reloader";
	public static final String PERM_NODE = "nox.reload";
	
	private PermissionHandler handler;
	private NoxCore core;
	private static final String ENTER_TREE = ">";
	
	public ReloadCommand(){
		core = NoxCore.getInstance();
		handler = core.getPermissionHandler();
	}
	
	public String getName() {
		return COMMAND_NAME;
	}
	
	private void nextTree(MessageBuilder mb, Reloader module, int level)
	{
		if (level != 0)
			mb.newLine();
		for (int i = 0; i < (level); i++)
			mb.append("-");
		if (level > 0)
			mb.append(ENTER_TREE);
		
		mb.append(module.getName());
		if (module.hasModules())
			for (Reloader subModule : module.getModules())
				nextTree(mb, subModule, level + 1);
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).append(' ').red("<<ModuleName> ").aqua("[SubModule ...]").red(">").newLine();
		mb.gray("Put * on the end of any module to specify to load all sub modules and self").newLine();
		mb.blue("Current Module Tree");
		MasterReloader mr = core.getMasterReloader();
		mb.newLine();
		if (mr.hasModules())
			for (Reloader module : mr.getModules())
				nextTree(mb, module, 0);
		else
			mb.red("No Modules Loaded?!");
		
		return mb.lines();
	}

	public boolean execute(CommandContext context) {
		
		CommandSender sender = context.getSender();
		String[] args = context.getArguments();
		
		if (!handler.hasPermission(sender, PERM_NODE))
			throw new NoPermissionException(sender, PERM_NODE, "You may not use this command!");
		
		if (context.getFlag("?", false)|| context.getFlag("h", false)|| context.getFlag("help", false) || args.length == 0)
		{
			displayHelp(sender);
			return true;
		}
		
		
		
		String module = null;
		if (args.length > 1)
			module = StringUtil.join(":", args);
		else if (args.length == 1)
			module = args[0];
		
		boolean all = false;
		if (module != null)
			all = module.endsWith("*");
		
		if (all)
			module = module.substring(0, module.length()-1);
		
		Reloader r = null;
		if (module == "" || module.length() == 0 && all)
			r = core.getMasterReloader();
		else
			r = core.getMasterReloader().getModule(module);
		
		try {
			if (all)
			{
				r.reload();
				r.reloadAll();
				MessageBuilder mb = new MessageBuilder(StringUtil.ampToColor(core.getGlobalLocale("command.successful", "Reloaded modules ->")));
				nextTree(mb, r, 0);
				sender.sendMessage(mb.lines());
			}
			else {
				r.reload();
				sender.sendMessage(StringUtil.ampToColor(core.getGlobalLocale("command.successful", "Module \"" + module + "\" reloaded!")));
			}
		} catch (NullPointerException e) {
			sender.sendMessage(StringUtil.ampToColor(core.getGlobalLocale("command.failed", "Module does not exist. \"" + module + "\"")));
		} catch (Exception e) {
			sender.sendMessage(StringUtil.ampToColor(core.getGlobalLocale("command.failed", "An error occured: " + e.getMessage())));
			e.printStackTrace();
		}
		return true;
	}

	public void displayHelp(CommandSender sender) {
		MessageBuilder mb = new MessageBuilder();
		
		mb.setSeparator("\n");
		for (String line : GlobalLocale.HELP_HEADER.get("Core", COMMAND_NAME).split("\n"))
			mb.append(line);
		for (String line : getHelp())
			mb.append(line);
		
		MessageUtil.sendMessage(sender, mb.lines());
	}

	public String[] getDescription() {
		return new String[0];
	}

	public String[] getFlags() {
		return new String[]{"help", "h", "?"};
	}

}
