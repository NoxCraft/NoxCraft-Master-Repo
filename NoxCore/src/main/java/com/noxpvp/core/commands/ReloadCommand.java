package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.MasterReloader;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.reloader.Reloader;

public class ReloadCommand extends BaseCommand {
	private NoxCore core;
	
	public static final String COMMAND_NAME = "reloader";
	
	private PermissionHandler handler;
	public ReloadCommand(){
		super(COMMAND_NAME, false);
		core = NoxCore.getInstance();
		handler = core.getPermissionHandler();
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
				MessageBuilder mb = new MessageBuilder(GlobalLocale.COMMAND_SUCCESS.get("Reloaded modules ->"));
				nextTree(mb, r, 0);
				sender.sendMessage(mb.lines());
			}
			else {
				r.reload();
				GlobalLocale.COMMAND_SUCCESS.message(sender, "Module \"" + module + "\" reloaded!");
			}
		} catch (NullPointerException e) {
			GlobalLocale.COMMAND_FAILED.message(sender, "Module does not exist. \"" + module + "\"");
		} catch (Exception e) {
			GlobalLocale.COMMAND_FAILED.message(sender, "An error occured: " + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}
	
	public String[] getDescription() {
		return new String[0];
	}
	
	public String[] getFlags() {
		return new String[]{"help", "h", "?"};
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
				nextTree(mb.white(" "), module, 0);
		else
			mb.red("No Modules Loaded?!");
		
		return mb.lines();
	}

	public int getMaxArguments() {
		return -1;
	}

	public NoxPlugin getPlugin() {
		return core;
	}

	private void nextTree(MessageBuilder mb, Reloader module, int level)
	{
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

	private static final String ENTER_TREE = ">";

	public static final String PERM_NODE = "nox.reload";

}
