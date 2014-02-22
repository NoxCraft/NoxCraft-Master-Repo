package com.noxpvp.mmo.command.subcommands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.gui.ClassChooseMenu;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassSwitchCommand extends BaseCommand {

	public static final String COMMAND_NAME = "switch";

	public ClassSwitchCommand() {
		super(COMMAND_NAME, true);
	}

	public String[] getFlags() {
		return new String[] {"h", "help", "nogui"};
	}

	public String[] getHelp() {
		MessageBuilder mb = new MessageBuilder(GlobalLocale.HELP_HEADER.get("NoxMMO : Classes", COMMAND_NAME));
		mb.newLine().green("Flags").newLine().setIndent(1);
		boolean first = true;
		mb.red("");
		for (String s : getFlags())
		{
			mb.append(s);
			if (first) {
				first = false;
				mb.setSeparator(", ");
			}
		}
		mb.setIndent(0).setSeparator("");
		return mb.lines();
	}

	public int getMaxArguments() {
		return 2;
	}

	@Override
	public boolean execute(CommandContext context) throws NoPermissionException {
		if (!context.hasFlag("nogui"))
		{
			new ClassChooseMenu(context.getPlayer(), null).show();
			return true;
		}
		
		String cName = null;
		String sTier = null;
		if (!context.hasArgument(0))
			return false;
		cName = context.getArgument(0);
		
		if (context.hasArgument(1))
			sTier = context.getArgument(1);
		
		if (sTier == null)
			sTier = "-1";
		
		int tier = ParseUtil.parseInt(sTier, -1);

		if (PlayerClassUtil.hasClassName(cName))
			cName = PlayerClassUtil.getIdByClassName(cName);
		
		if (!PlayerClassUtil.hasClassId(cName))
		{
			MMOLocale.CLASS_NONE_BY_NAME.message(context.getSender(), cName);
			return true;
		}
		
		PlayerClass c = PlayerClassUtil.safeConstructClass(cName, context.getPlayer());
		
		MMOPlayer p =  PlayerManager.getInstance().getPlayer(context.getPlayer());
		
		if (tier == -1)
			c.setCurrentTier(c.getLatestTier());
		
		if (!c.canUseTier(tier))
		{
			MMOLocale.CLASS_TIER_LOCKED.message(context.getSender(), cName, ""+ tier, "(RAW) class.canUseTier(" + tier + ") == false");
			return true;
		}
		c.setCurrentTier(tier);
		
		p.setClass(c);
		
		return true;
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

}
