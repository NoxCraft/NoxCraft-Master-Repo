package com.noxpvp.mmo.prism;

import com.noxpvp.core.external.prism.BaseNoxPrismActionType;

public class UsedAbilityActionType extends BaseNoxPrismActionType {

	public static final String name = "noxmmo-ability-abilityuse";
	public static final String verb = "used ability";
	
	public UsedAbilityActionType() {
		super(name, false, false, false, "AbilityUseAction", verb);
		
	}

}
