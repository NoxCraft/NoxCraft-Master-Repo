package com.noxpvp.core.commands;

import java.util.Map;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.MasterReloader;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.reloader.Reloader;

public class ReloadCommand implements DescriptiveCommandRunner {
	public final static String COMMAND_NAME = "reload";
	private NoxCore core;
	private final static String ENTER_TREE = ">";
	
	public ReloadCommand(){
		core = NoxCore.getInstance();
	}
	
	public String getName() {
		return COMMAND_NAME;
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

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.yellow("[").blue("NoxCore ").aqua("Reloader").yellow("]").newLine();
		mb.blue("/").append(COMMAND_NAME).append(' ').yellow("[[ModuleName],...]").newLine();
		mb.yellow("Put * on the end of any module to specify to load all sub modules and self");
		mb.blue("Current Module Tree");
		MasterReloader mr = core.getMasterReloader();
		
		if (mr.hasModules())
			for (Reloader module : mr.getModules())
				nextTree(mb, module, 0);
		else
			mb.red("No Modules Loaded?!");
		
		return mb.lines();
	}

	public boolean execute(CommandSender sender, Map<String, Object> flags, String[] args) {
		if (flags.containsKey("?") || flags.containsKey("help") || flags.containsKey("h"))
		{
			displayHelp(sender);
			return true;
		}
		
		String module = StringUtil.combine("\\", args);
//		if (module.equals(""))
//		{
//			displayHelp(sender);
//			return true;
//		}
		
		boolean all = module.endsWith("*");
		if (all)
			module = module.substring(0, module.length()-1);
		
		Reloader r = core.getMasterReloader().getModule(module);
		
		try {
			if (all)
			{
				r.reload();
				r.reloadAll();
				MessageBuilder mb = new MessageBuilder(StringUtil.ampToColor(core.getLocale("command.successful", "Reloaded modules...")));
				mb.newLine();
				nextTree(mb, r, 0);
				sender.sendMessage(mb.lines());
			}
			else {
				r.reload();
				sender.sendMessage(StringUtil.ampToColor(core.getLocale("command.successful", "Module \"" + module + "\" reloaded!")));
			}
		} catch (NullPointerException e) {
			sender.sendMessage(StringUtil.ampToColor(core.getLocale("command.failed", "Module does not exist. \"" + module + "\"")));
		} catch (Exception e) {
			sender.sendMessage(StringUtil.ampToColor(core.getLocale("command.failed", "An error occured: " + e.getMessage())));
			e.printStackTrace();
		}
		return true;
	}

	public void displayHelp(CommandSender sender) {
		for (String line : getHelp())
			sender.sendMessage(line);
	}

	public String[] getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getFlags() {
		return new String[]{"help", "h", "?"};
	}

}
