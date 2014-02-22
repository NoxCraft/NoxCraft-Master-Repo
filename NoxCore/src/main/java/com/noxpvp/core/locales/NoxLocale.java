package com.noxpvp.core.locales;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.localization.LocalizationEnum;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.utils.chat.MessageUtil;

public abstract class NoxLocale extends LocalizationEnum {

	public NoxLocale(String name, String defValue) {
		super(name, defValue);
	}
	
	@Override
	public String get(String... args) {
		return MessageUtil.parseColor(getPlugin().getLocale(getName(), args));
	}
	
	public final String getSafe(String... args)
	{
		return MessageUtil.parseArguments(MessageUtil.parseColor(get()), args);
	}
	
	public abstract NoxPlugin getPlugin();
	
	@Override
	public void message(CommandSender sender, String... args) {
		MessageUtil.sendMessage(sender, getSafe(args).split("\n")); //TODO: Discuss if we should ignore messages if Blank
	}
	
	public boolean isEmpty(String... args) {
		String m = get(args);
		return (m.isEmpty() || m.length()==0);
	}
}
