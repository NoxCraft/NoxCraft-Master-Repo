package com.noxpvp.homes.locale;

import com.noxpvp.core.locales.NoxLocale;
import com.noxpvp.homes.NoxHomes;

public class HomeLocale extends NoxLocale {

	public final static HomeLocale LIST_OWN 	= new HomeLocale("homes.list.own", "&3Your Homes&r: &e%1%");
	public final static HomeLocale LIST_OTHERS 	= new HomeLocale("homes.list.default", "&e%0%'s &3homes: &e%1%");
	
	public final static HomeLocale HOME_OWN 	= new HomeLocale("homes.home.own", "&3You teleported to home: %1%");
	public final static HomeLocale HOME_OTHERS 	= new HomeLocale("homes.home.default", "&3You teleported to %0%'s home named &e%1%");
	
	public final static HomeLocale DELHOME_OWN		= new HomeLocale("homes.delhome.own", "&cRemoved your home:&e%1%");
	public final static HomeLocale DELHOME_OTHERS	= new HomeLocale("homes.delhome.default", "&cDeleted &e%0%'s&c home named &e%1%");
	
	public final static HomeLocale SETHOME_OWN		=new HomeLocale("homes.sethome.own", "&aSet new home named &e%1%&a at &6%2%");
	public final static HomeLocale SETHOME_OTHERS	= new HomeLocale("homes.sethome.default", "&aSet new home for &e%0%&2 &anamed: &e%1%&a at &6%2%");
	
	public final static HomeLocale WARMUP	= new HomeLocale("homes.warmup", "&3Warmup started. &cDo not move!"); //%0% is the time to warmup in seconds
	public final static HomeLocale COOLDOWN	= new HomeLocale("homes.cooldown", "&4Your too tired to get home.&e Wait another &4%0%&e seconds");
	
	public HomeLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxHomes getPlugin() {
		return NoxHomes.getInstance();
	}

}
