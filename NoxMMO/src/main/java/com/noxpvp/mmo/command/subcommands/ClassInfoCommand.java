package com.noxpvp.mmo.command.subcommands;

import java.util.Map.Entry;
import java.util.Set;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.utils.NoxMessageBuilder;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.IClassTier;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.util.NoxMMOMessageBuilder;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassInfoCommand extends BaseCommand {

	public static final String COMMAND_NAME = "info";

	public ClassInfoCommand() {
		super(COMMAND_NAME, false);
	}
	
	public String[] getFlags() {
		return new String[] {};
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public CommandResult execute(CommandContext context) throws NoPermissionException {
		
		if (!context.hasArgument(0))
			return new CommandResult(this, false);
		
		String className = context.getArgument(0).toLowerCase();
		
		if (!PlayerClassUtil.hasClassNameIgnoreCase(className))
			return new CommandResult(this, false);
		
		PlayerClass clazz = null;
		for (PlayerClass c : PlayerClassUtil.getAvailableClasses(context.getPlayer()))
			if (c.getName().equalsIgnoreCase(className)) {
				clazz = c;
				break;
			}
		
		if (clazz == null)
			return new CommandResult(this, false);
		
		NoxMMOMessageBuilder mb = new NoxMMOMessageBuilder(getPlugin());
		mb.commandHeader(clazz.getDisplayName() + " Class", true);
		
		mb.withClassInfo(clazz).headerClose(true);
		mb.send(context.getSender());
		
		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
