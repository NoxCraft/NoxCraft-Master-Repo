package com.noxpvp.core.locales;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;

public class CoreLocale extends NoxLocale {

	public final static CoreLocale GROUP_TAG_PREFIX;
	public final static CoreLocale GROUP_TAG_SUFFIX;
	
	static {
		GROUP_TAG_PREFIX = new CoreLocale("special.ptag.prefix", "");
		GROUP_TAG_SUFFIX = new CoreLocale("special.ptag.suffix", "");
	}
	
	public CoreLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}
}
