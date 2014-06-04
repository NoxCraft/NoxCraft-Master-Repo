package com.noxpvp.mmo.prism;

import com.noxpvp.core.utils.PrismUtil;
import com.noxpvp.mmo.NoxMMO;

public class MMOPrismUtil {

	public static void registerActionTypes() {
		PrismUtil.registerActionType(NoxMMO.getInstance(), new UsedAbilityActionType());
		
	}
	
	public static void registerCustomHandlers() {
		PrismUtil.registerCustomActionHandler(NoxMMO.getInstance(), AbilityUseAction.class);
		
	}

}
