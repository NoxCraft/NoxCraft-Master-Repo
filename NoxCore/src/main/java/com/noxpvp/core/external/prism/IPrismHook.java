package com.noxpvp.core.external.prism;

import com.noxpvp.core.NoxPlugin;

import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.GenericAction;
import me.botsko.prism.events.PrismCustomPlayerActionEvent;

public interface IPrismHook {

	public boolean isPrismEnabled();
	
	public void registerActionType(NoxPlugin plugin, ActionType type);
	
	public void registerCustomActionHandler(NoxPlugin plugin, Class<? extends GenericAction> clazz);

	public void callCustomPrismEvent(PrismCustomPlayerActionEvent event);

}
