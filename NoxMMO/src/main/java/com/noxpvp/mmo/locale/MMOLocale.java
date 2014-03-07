package com.noxpvp.mmo.locale;

import com.noxpvp.core.locales.NoxLocale;
import com.noxpvp.mmo.NoxMMO;

public class MMOLocale extends NoxLocale {
	
	//Class
	public final static MMOLocale CLASS_NONE_BY_NAME;
	public final static MMOLocale CLASS_LOCKED;
	public final static MMOLocale CLASS_TIER_LOCKED;
	public final static MMOLocale CLASS_NO_TIER;

	//ABILITIES
	public final static MMOLocale ABIL_DISPLAY_NAME;
	
	//GUI
	public final static MMOLocale GUI_MENU_NAME_COLOR;
	
	//INTERNAL
	/**
	 * @deprecated This is used internally. Do not use!
	 */
	public final static MMOLocale ABIL_DN;

	static {
		ABIL_DN = new MMOLocale("display.ability.default", "%1%%0%");
		ABIL_DISPLAY_NAME = new MMOLocale("display.ability", ""); //This is blank. Will be filled in per ability name.
		
		GUI_MENU_NAME_COLOR = new MMOLocale("gui.inventory.name_color", "&6");
		
		CLASS_NONE_BY_NAME = new MMOLocale("error.class.none-by-name", "&4There is no class by the name of %0%");
		CLASS_LOCKED = new MMOLocale("error.class.locked", "&4The class %0% is locked. &4Reason:&e %1%");
		CLASS_TIER_LOCKED = new MMOLocale("error.class.tier.locked", "&4The class %0% tier %1% is locked. &4Reason:&e %2%");
		CLASS_NO_TIER = new MMOLocale("error.class.tier.missing", "&4The class %0% does not have a tier %1%");
	}
	
	public MMOLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
}
