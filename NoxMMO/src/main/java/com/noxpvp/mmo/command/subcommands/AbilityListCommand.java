package com.noxpvp.mmo.command.subcommands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.locale.MMOLocale;

public class AbilityListCommand extends BaseCommand {

	public static final String COMMAND_NAME = "list";

	public AbilityListCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return new String[0];
	}

	public String[] getHelp() {
		return null;
	}

	public int getMaxArguments() {
		return 0;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {
		
		
		MMOPlayer player = PlayerManager.getInstance().getPlayer(context.getPlayer());
		MessageBuilder mb = new MessageBuilder(MMOLocale.ABIL_LIST_HEADER.get("from your classes"));
		
		boolean first = true;
		for (Ability ability: player.getAllAbilities()) {
			mb.append(ability.getDisplayName());
			if (first) { mb.setSeparator(MMOLocale.ABIL_LIST_SEPERATOR.get()); first = false;}
		}
		mb.setSeparator("");
		mb.send(context.getSender());
		
		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
