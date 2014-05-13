package com.noxpvp.core.utils;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.BaseCommand;
import com.noxpvp.core.commands.NoxCommand;
import com.noxpvp.core.locales.GlobalLocale;

public class NoxMessageBuilder extends MessageBuilder {
	
	private NoxPlugin plugin;
	
	public NoxMessageBuilder(NoxPlugin plugin) {
		this(plugin, false);
	}

	public NoxMessageBuilder(NoxPlugin plugin, boolean withHeader) {
		super();
		
		this.plugin = plugin;
		helpHeader(true).newLine();
	}
	
	public NoxMessageBuilder helpHeader() {
		return helpHeader(false);
	}
	
	public NoxMessageBuilder helpHeader(boolean addNewline) {
		append(GlobalLocale.HELP_HEADER.get(plugin.getName()));
		if (addNewline)
			newLine();
		
		return this;
	}
	
	public NoxMessageBuilder headerClose() {
		return headerClose(false);
	}
	
	public NoxMessageBuilder headerClose(boolean newlineFirst) {
		if (newlineFirst)
			newLine();
		
		return (NoxMessageBuilder) append(GlobalLocale.HEADER_CLOSE.get());
	}
	
	public NoxMessageBuilder withCommand(NoxCommand command) {
		return withCommand(command, false);
	}
	
	public NoxMessageBuilder withCommand(BaseCommand command, boolean addNewline) {
		gold("/")
		.yellow(command.getFullName())
		.append(" ");
		
		if (command.getFlags().length > 0)
			inBrackets(command.getFlags());
		
		if (addNewline)
			newLine();
		
		return this;
	}
	
	public NoxMessageBuilder inBrackets(String... text) {
		return (NoxMessageBuilder) gold("[").yellow(StringUtil.join(", ", text)).gold("]");
	}
	
	private NoxMessageBuilder cast(MessageBuilder mb) {
		if (mb instanceof NoxMessageBuilder)
			return (NoxMessageBuilder) mb;
		else
			throw new IllegalArgumentException("Cannot cast a real MessageBuilder fool!");
	}
	
}
