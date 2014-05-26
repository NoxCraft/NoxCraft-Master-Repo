package com.noxpvp.core.external.prism;

import com.noxpvp.core.NoxPlugin;

import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.GenericAction;
import me.botsko.prism.events.PrismCustomPlayerActionEvent;

public class NullPrismHook implements IPrismHook {

	public boolean isPrismEnabled() {
		return false;
	}

	public void registerActionType(NoxPlugin plugin, ActionType type) {
		return;
	}
	
	public void registerCustomActionHandler(NoxPlugin plugin, Class<? extends GenericAction> clazz) {
		return;
	}
	
	public void callCustomPrismEvent(PrismCustomPlayerActionEvent event) {
		return;
	}

}
