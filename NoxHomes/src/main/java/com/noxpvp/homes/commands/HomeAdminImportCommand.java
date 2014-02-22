package com.noxpvp.homes.commands;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.utils.chat.MessageUtil;
import com.noxpvp.homes.PlayerManager;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.homes.HomeImporter;

public class HomeAdminImportCommand extends BaseCommand {
	public static final String COMMAND_NAME = "import";
	public static final String PERM_NODE = "import";
	private PlayerManager manager;
	private NoxHomes plugin;
	
	private final PermissionHandler permHandler;
	private String[] importerNames;
	
	
	public HomeAdminImportCommand()
	{
		super(COMMAND_NAME, false);
		
		if (NoxCore.getInstance() == null)
			throw new RuntimeException("NoxCore plugin is not loaded! Do not use this class in other plugins please.");
		
		this.plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
		
		HomeImporter[] vals = HomeImporter.values();
		importerNames = new String[vals.length];
		
		for (int i = 0; i < importerNames.length; i++)
			importerNames[i] = vals[i].name();
	}
	
	public boolean execute(CommandContext context) {
		CommandSender sender = context.getSender();
		String[] args = context.getArguments();
		if (manager == null)
		{
			manager = plugin.getHomeManager();
			if (manager == null);
			{
				MessageUtil.sendLocale(sender, GlobalLocale.ERROR_NULL, "PlayerManager reference in Home List Object.");
				return true;
			}
		}
		
		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, "admin", PERM_NODE);
		if (!permHandler.hasPermission(sender, perm))
		{
			MessageUtil.sendLocale(sender, GlobalLocale.FAILED_PERMISSION, "Can not import homes data.", perm);
			return true;
		}
		
		
		
		if (args.length < 1 || args[0].equalsIgnoreCase("help") || context.hasFlag("h") || context.hasFlag("help") || HomeImporter.valueOf(args[0]) == null)
		{
			
			MessageBuilder mb = new MessageBuilder();
			
			if (args.length < 1)
				mb.red("You must specify one of the importers.").newLine();
			else if (HomeImporter.valueOf(args[0]) == null)
				mb.red(args[0]).append(" is not a valid importer.");
			mb.blue("List of importers: ");
			
			mb.yellow("[").green(StringUtil.combineNames(importerNames)).yellow("]").newLine().aqua("/").append(COMMAND_NAME).append(" ").yellow("[").green(StringUtil.join("|", importerNames)).yellow("]");
			
			mb.send(sender);
			return true;
		}
		MessageBuilder mb = new MessageBuilder();
		HomeImporter porter = null;
		if (args.length > 0)
			porter = HomeImporter.valueOf(args[0]);
		
		if (porter == null)
		{
			mb.red("Importer: ").append(args[0]).append(". Does not exist.");
			mb.newLine().aqua("The following importers are available. ").yellow("[").green(StringUtil.combineNames(importerNames)).yellow("]");
		} else {
			boolean success = porter.importData(context.hasFlag("e") || context.hasFlag("erase") || context.hasFlag("overwrite"));
			if (success)
				mb.gold("Imported data from ").append(args[0]).append(" successfully!");
			else
				mb.red("Could not import data. For more information view console logs.");
		}
		
		mb.send(sender);
		return true;
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder();
		mb.blue("/").append(HomeAdminCommand.COMMAND_NAME).append(" ").append(COMMAND_NAME).append(" ").yellow("[importerName]");
		mb.newLine().blue("Importers: ").newLine();
		mb.aqua("[").green(StringUtil.combineNames(importerNames)).aqua("]");
		
		return mb.lines();
	}

	public String[] getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("Imports data from another homes type plugin.");

		return sb.toString().split(System.lineSeparator());
	}

	public String[] getFlags() {
		return new String[]{"e", "erase", "overwrite", "h", "help"};
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public NoxPlugin getPlugin() {
		return plugin;
	}
}
