package com.noxpvp.mmo.locale;

import com.noxpvp.core.locales.NoxLocale;
import com.noxpvp.mmo.NoxMMO;

public class MMOLocale extends NoxLocale {
	
	//Error
	/**
	 * @param 1 class
	 */
	public final static MMOLocale CLASS_NONE_BY_NAME;
	
	/**
	 * @param 1 class
	 * @param 2 reason
	 */
	public final static MMOLocale CLASS_LOCKED;
	
	/**
	 * @param 1 class
	 * @param 2 reason
	 */
	public final static MMOLocale CLASS_TIER_LOCKED;
	
	/**
	 * @param 1 class
	 * @param 2 tier
	 */
	public final static MMOLocale CLASS_NO_TIER;
	
	/**
	 * @param 1 ability
	 * @param 2 remaining time
	 */
	public final static MMOLocale ABIL_ON_COOLDOWN;
	
	/**
	 * @param 1 ability
	 * @param 2 target
	 */
	public final static MMOLocale ABIL_BAD_TARGET;
	
	/**
	 * @param none
	 */
	public final static MMOLocale ABIL_NO_TARGET;
	
	/**
	 * @param 1 ability
	 * @param 2 town/region/other name
	 */
	public final static MMOLocale ABIL_NO_PVP_ZONE;

	//Display
	public final static MMOLocale CLASS_DN;
	public final static MMOLocale CLASS_DISPLAY_NAME;
	public final static MMOLocale ABIL_DISPLAY_NAME;
	
	/**
	 * @param 1 ability
	 */
	public final static MMOLocale ABIL_USE;
	
	/**
	 * @param 1 ability
	 * @param 2 target
	 */
	public final static MMOLocale ABIL_USE_TARGET;
	
	/**
	 * @param 1 ability
	 * @param 2 target
	 * @param 3 damage amount (format %.2f please)
	 */
	public final static MMOLocale ABIL_USE_TARGET_DAMAGED;
	
	/**
	 * @param 1 ability
	 */
	public final static MMOLocale ABIL_HIT;
	
	/**
	 * @param 1 attacker
	 * @param 2 ability
	 */
	public final static MMOLocale ABIL_HIT_ATTACKER;
	
	/**
	 * @param 1 attacker
	 * @param 2 ability
	 * @param 3 damage amount (format %.2f please)
	 */
	public final static MMOLocale ABIL_HIT_ATTACKER_DAMAGED;
	
	/**
	 * @param 1 Ability name
	 * @param 2 Description
	 */
	public final static MMOLocale ABIL_INFO;
	
	//Guis
	public final static MMOLocale GUI_MENU_NAME_COLOR;
	public final static MMOLocale GUI_BAR_COLOR;
	
	//INTERNAL
	/**
	 * @deprecated This is used internally. Do not use!
	 */
	public final static MMOLocale ABIL_DN;

	static {
		//Error
		CLASS_NONE_BY_NAME = new MMOLocale("error.class.none-by-name", "&cThere is no class by the name of &e%0%");
		CLASS_LOCKED = new MMOLocale("error.class.locked", "&cThe class &e%0% &cis locked: &e%1%");
		CLASS_TIER_LOCKED = new MMOLocale("error.class.tier.locked", "&cThe &e%0% &ctier is locked: &e%1%");
		CLASS_NO_TIER = new MMOLocale("error.class.tier.missing", "&cThe class &e%0% &cdoes not have a &e%1% &ctier");
		ABIL_ON_COOLDOWN = new MMOLocale("error.ability.on-cooldown", "&cThe &e%0% &cability has a cooldown of &e%1% &cremaining");
		ABIL_BAD_TARGET = new MMOLocale("error.ability.bad-target", "&cThe &e%0% &cability cannot be used on &e%1% &cright now");
		ABIL_NO_TARGET = new MMOLocale("error.ability.no-target", "&cYou don't have a target right now!");
		ABIL_NO_PVP_ZONE = new MMOLocale("error.ability.pvp-zone", "&cYou can't use the &e%0% &cability in &e%1%");
		
		//Display
		CLASS_DN = new MMOLocale("display.class.default", "%1%%0%");
		CLASS_DISPLAY_NAME = new MMOLocale("display.class", "");
		ABIL_DN = new MMOLocale("display.ability.default", "%1%%0%");
		ABIL_DISPLAY_NAME = new MMOLocale("display.ability", "");
		ABIL_INFO = new MMOLocale("display.ability.info", "&e%0%&6: &c%1%");
		ABIL_USE = new MMOLocale("display.ability.used", "&6You used the &e%0% &6ability!");
		ABIL_USE_TARGET = new MMOLocale("display.ability.used-on-target", "&6You used the &e%0% &6ability on &c%1%");
		ABIL_USE_TARGET_DAMAGED = new MMOLocale("display.ability.used-on-target-damaged", "&6You used the &e%0% &6ability on &c%1%&6, dealing &c%2% &6damage!");
		ABIL_HIT = new MMOLocale("display.ability.hit", "&6You were hit by &e%1%&6!");
		ABIL_HIT_ATTACKER = new MMOLocale("display.ability.hit-attacker", "&c%0% &6used &e%1% &6on you!");
		ABIL_HIT_ATTACKER_DAMAGED = new MMOLocale("display.ability.hit-attacker-damaged", "&c%0% &6used &e%1% &6on you! dealing &c%2% &cdamage");
		
		
		//GUIs
		GUI_MENU_NAME_COLOR = new MMOLocale("gui.inventory.name_color", "&6");
		GUI_BAR_COLOR = new MMOLocale("gui.target_bar.color", "&a");		
		
	}
	
	public MMOLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}
}
