package com.noxpvp.core.external.prism;

import org.bukkit.entity.Player;

import com.noxpvp.core.NoxPlugin;

import me.botsko.prism.events.PrismCustomPlayerActionEvent;

public class BaseNoxPrismEvent extends PrismCustomPlayerActionEvent implements INoxPrismEvent {

	private NoxPlugin plugin;
	
	/**
	 * 
	 * @param plugin
	 * @param action_type_name
	 * @param player
	 * @param builtArgBuilder see {@link NoxPrismEventArgBuilder}
	 */
	public BaseNoxPrismEvent(NoxPlugin plugin, String action_type_name,
			Player player, String builtArgBuilder) {
		
		super(plugin, action_type_name, player, builtArgBuilder);
		
		this.plugin = plugin;
	}

	public NoxPlugin getPlugin() {
		return plugin;
	}

}
