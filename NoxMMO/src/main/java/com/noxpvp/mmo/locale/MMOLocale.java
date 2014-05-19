package com.noxpvp.mmo.locale;

import com.noxpvp.core.locales.NoxLocale;
import com.noxpvp.mmo.NoxMMO;

public class MMOLocale extends NoxLocale {
	
	//Class
	public final static MMOLocale CLASS_NONE_BY_NAME;
	public final static MMOLocale CLASS_LOCKED;
	public final static MMOLocale CLASS_TIER_LOCKED;
	public final static MMOLocale CLASS_NO_TIER;

	//CLASS DISPLAY
	public final static MMOLocale CLASS_DN;
	public final static MMOLocale CLASS_DISPLAY_NAME;

	//ABILITIES DISPLAY
	public final static MMOLocale ABIL_DISPLAY_NAME;
	public final static MMOLocale ABIL_LIST_SEPERATOR;
	public final static MMOLocale ABIL_LIST_HEADER;
	
	//GUI
	public final static MMOLocale GUI_MENU_NAME_COLOR;
	public final static MMOLocale GUI_BAR_COLOR;
	
	//Ability Information
	public final static MMOLocale ABIL_INFO;
	
	//INTERNAL
	/**
	 * @deprecated This is used internally. Do not use!
	 */
	public final static MMOLocale ABIL_DN;

	static {
		ABIL_DN = new MMOLocale("display.ability.default", "%1%%0%");
		ABIL_DISPLAY_NAME = new MMOLocale("display.ability", ""); //This is blank. Will be filled in per ability name.
		
		ABIL_INFO = new MMOLocale("display.ability.info", "&7Ability: &e%0%&a %1%");
		
		ABIL_LIST_SEPERATOR = new MMOLocale("display.ability.list.color", "&7,");
		ABIL_LIST_HEADER = new MMOLocale("display.ability.list.header", "&eList Of Abilities %0%");
		
		CLASS_DN = new MMOLocale("display.class.default", "%1%%0%");
		CLASS_DISPLAY_NAME = new MMOLocale("display.class", ""); //This is blank. Will be filled in per class name.
		
		GUI_MENU_NAME_COLOR = new MMOLocale("gui.inventory.name_color", "&6");
		GUI_BAR_COLOR = new MMOLocale("gui.target_bar.color", "&a");
		
		CLASS_NONE_BY_NAME = new MMOLocale("error.class.none-by-name", "&cThere is no class by the name of %0%");
		CLASS_LOCKED = new MMOLocale("error.class.locked", "&cThe class %0% is locked. &cReason:&e %1%");
		CLASS_TIER_LOCKED = new MMOLocale("error.class.tier.locked", "&cThe %0% class tier %1% is locked. &cReason:&e %2%");
		CLASS_NO_TIER = new MMOLocale("error.class.tier.missing", "&cThe class %0% does not have a tier %1%");
	}
	
	public MMOLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
}
