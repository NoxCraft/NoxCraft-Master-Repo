package com.noxpvp.mmo.command.subcommands;

import org.bukkit.ChatColor;

import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.utils.NoxMessageBuilder;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.Ability;

public class AbilityListCommand extends BaseCommand {

	public static final String COMMAND_NAME = "list";

	public AbilityListCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return new String[0];
	}

	public int getMaxArguments() {
		return 0;
	}

	@Override
	public CommandResult execute(CommandContext context)
			throws NoPermissionException {

		MMOPlayer player = MMOPlayerManager.getInstance().getPlayer(context.getPlayer());
		NoxMessageBuilder mb = new NoxMessageBuilder(getPlugin()).commandHeader("Ability List", true);

		for (Ability ability : player.getAllAbilities()) {
			mb.yellow(ability.getDisplayName()).gold(": ").newLine();
			for (String lore : ability.getLore(ChatColor.RED, 30))
				mb.append(lore).newLine();
		}

		mb.headerClose();
		mb.send(context.getSender());

		return new CommandResult(this, true);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
