package com.noxpvp.core.external.prism;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.GenericAction;
import me.botsko.prism.events.PrismCustomPlayerActionEvent;
import me.botsko.prism.exceptions.InvalidActionException;

import com.noxpvp.core.NoxPlugin;

public class PrismHook implements IPrismHook {
	
	private Prism prism;
	private NoxPlugin plugin;
	
	public PrismHook (Prism prism, NoxPlugin plugin) {
		this.prism = prism;
		this.plugin = plugin;
	}

	public boolean isPrismEnabled() {
		return prism != null && prism.isEnabled();
	}
	
	public void registerActionType(NoxPlugin plugin, ActionType type) {
		if (isPrismEnabled())
			try {
				Prism.getActionRegistry().registerCustomAction(plugin, type);
			} catch (InvalidActionException e) {
				plugin.log(Level.WARNING, "Could not register custom ActionType in prism: " + e.getMessage());
				e.printStackTrace();
			}
	}
	
	public void registerCustomActionHandler(NoxPlugin plugin, Class<? extends GenericAction> clazz) {
		if (isPrismEnabled())
			try {
				Prism.getHandlerRegistry().registerCustomHandler(plugin, clazz);
			} catch (InvalidActionException e) {
				plugin.log(Level.WARNING, "Could nat register a custom Handler in prism: " + e.getMessage());
				e.printStackTrace();
			}
		
	}
	
	public void callCustomPrismEvent(PrismCustomPlayerActionEvent event) {
		if (isPrismEnabled())
			Bukkit.getPluginManager().callEvent(event);
	}

}
