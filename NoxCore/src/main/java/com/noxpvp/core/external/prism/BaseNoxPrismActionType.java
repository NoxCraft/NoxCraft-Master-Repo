package com.noxpvp.core.external.prism;

import me.botsko.prism.actionlibs.ActionType;

public class BaseNoxPrismActionType extends ActionType {
	
	public BaseNoxPrismActionType(String name, boolean doesCreateBlock, boolean canRollback,
			boolean canRestore, String handlerName, String verbDisc) {
		super(name, doesCreateBlock, canRollback, canRestore, handlerName, verbDisc);
	}
}
