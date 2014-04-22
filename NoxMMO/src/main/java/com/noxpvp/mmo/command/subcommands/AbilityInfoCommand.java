package com.noxpvp.mmo.command.subcommands;

import java.util.Map;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.commands.SafeNullPointerException;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.locale.MMOLocale;

public class AbilityInfoCommand extends BaseCommand {

	public static final String COMMAND_NAME = "info";

	public AbilityInfoCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return new String[] { "h", "help" };
	}

	public String[] getHelp() {
		return null;
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
		
		MMOPlayer mPlayer = PlayerManager.getInstance().getPlayer(context.getPlayer());
		
		if (mPlayer == null)
			return new CommandResult(this, true, new MessageBuilder().red("mPlayer object is null!").lines());
		
		Map<String, Ability> abilities = mPlayer.getAllMappedAbilities();
		
		Ability ability = null;
		if (abilities.containsKey(abilityName))
			ability = abilities.get(abilityName);
		
		if (ability == null)
			throw new SafeNullPointerException("Ability does not exist!");
		
		MessageUtil.sendLocale(context.getSender(), MMOLocale.ABIL_INFO, ability.getDisplayName(), ability.getDescription());
		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
