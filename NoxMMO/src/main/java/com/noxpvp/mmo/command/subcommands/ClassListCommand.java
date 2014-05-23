package com.noxpvp.mmo.command.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.util.NoxMMOMessageBuilder;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassListCommand extends BaseCommand {

	public final static String COMMAND_NAME = "list";
	
	public ClassListCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return new String[] {};
	}

	public int getMaxArguments() {
		return 0;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		
		NoxMMOMessageBuilder mb = new NoxMMOMessageBuilder(getPlugin(), true);
		Player p = context.getPlayer();
		
		for (PlayerClass clazz : PlayerClassUtil.getAvailableClasses(p)) {
			mb.yellow("Primary classes").gold(": ");
			
			List<String> names = new ArrayList<String>();
			if (clazz.isPrimaryClass())
				names.add(clazz.getDisplayName());
			
			mb.append(StringUtil.join(ChatColor.GOLD + ", ", names));
		}
		
		mb.newLine().newLine();
		for (PlayerClass clazz : PlayerClassUtil.getAvailableClasses(p)) {
			mb.yellow("Secondary classes").gold(": ");
			
			List<String> names = new ArrayList<String>();
			if (!clazz.isPrimaryClass())
				names.add(clazz.getDisplayName());
			
			mb.append(StringUtil.join(ChatColor.GOLD + ", ", names));
		}
		
		mb.headerClose(true);
		mb.send(p);
		
		return new CommandResult(this, true);
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxMMO.getInstance();
	}

}
