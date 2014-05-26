package com.noxpvp.core.utils;

import org.bukkit.Bukkit;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.GenericAction;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.external.prism.BaseNoxPrismEvent;
import com.noxpvp.core.external.prism.IPrismHook;
import com.noxpvp.core.external.prism.NullPrismHook;
import com.noxpvp.core.external.prism.PrismHook;
public class PrismUtil {
	private static ModuleLogger log;
	
	private static Prism prism;
	private static IPrismHook hook = new NullPrismHook();

	static {
		setup();
	}
	
	public static void setup() {
		if (log == null)
			log = NoxCore.getInstance().getModuleLogger("PrismUtil");

		prism = NoxCore.getInstance().getPrism();
		
		if (prism == null) {
			prism = (Prism) Bukkit.getPluginManager().getPlugin("Prism");
		}
		
		if (prism != null)
			hook = new PrismHook(prism, NoxCore.getInstance());
		else log.severe("Prism is null and could not be found");
	}
	
	public static void registerActionType(NoxPlugin plugin, ActionType type) {
		hook.registerActionType(plugin, type);
	}
	
	public static void registerCustomActionHandler(NoxPlugin plugin, Class<? extends GenericAction> clazz) {
		hook.registerCustomActionHandler(plugin, clazz);
	}
	
	public static void callCustomPrismEvent(BaseNoxPrismEvent event) {
		hook.callCustomPrismEvent(event);
	}
}
