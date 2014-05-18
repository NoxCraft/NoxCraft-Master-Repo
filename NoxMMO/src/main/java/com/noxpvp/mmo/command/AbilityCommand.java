package com.noxpvp.mmo.command;

import java.util.Map;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
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
		return new String[]{"/ability [info] <ability name>"};
	}

	public int getMaxArguments() {
		return 1;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		
		if (!context.hasArgument(1))
			return new CommandResult(this, false);
		
		String abilityName = context.getArgument(0);
		
		MMOPlayer mPlayer = MMOPlayerManager.getInstance().getPlayer(context.getPlayer());
		
		if (mPlayer == null)
			return new CommandResult(this, true, new MessageBuilder().red("mPlayer object is null!").lines());
		
		Map<String, Ability> abilities = mPlayer.getAllMappedAbilities();
		
		Ability ability = null;
		if (abilities.containsKey(abilityName))
			ability = abilities.get(abilityName);
		
		if (ability == null)
			throw new SafeNullPointerException("Ability does not exist!");
		
		if (ability.mayExecute())
			ability.execute();
		
		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
