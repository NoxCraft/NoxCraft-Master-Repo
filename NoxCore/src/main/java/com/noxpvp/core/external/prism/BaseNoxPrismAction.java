package com.noxpvp.core.external.prism;

import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actions.GenericAction;

public class BaseNoxPrismAction extends GenericAction {
	
	public BaseNoxPrismAction(ActionType type) {
		setActionType(type.getName());
		
	}

}
