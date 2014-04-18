package com.noxpvp.mmo.command;

import java.util.Map;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.command.subcommands.AbilityInfoCommand;

public class AbilityCommand extends BaseCommand {

	public static final String COMMAND_NAME = "ability";
	
	public AbilityCommand() {
		super(COMMAND_NAME, true);
		registerSubCommand(new AbilityInfoCommand());
	}

	public String[] getFlags() {
		return new String[] { "h", "help" };
	}

	public String[] getHelp() {
		return new String[]{"/ability <ability name>"};
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		
		MMOPlayer mPlayer = PlayerManager.getInstance().getPlayer(context.getPlayer());
		
		if (mPlayer == null)
			return new CommandResult(this, true, new MessageBuilder().red("mPlayer object is null!").lines());
		
		Map<String, Ability> abilities = mPlayer.getAllMappedAbilities();
		return null;
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
