package com.noxpvp.core.locales;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;

public class CoreLocale extends NoxLocale {

	public static final CoreLocale GROUP_TAG_PREFIX;
	public static final CoreLocale GROUP_TAG_SUFFIX;
	public static final CoreLocale SPECIAL_CHAT_PINGCOLOR;
	public static final CoreLocale SPECIAL_CHAT_PINGSYMBAL;

	static {
		GROUP_TAG_PREFIX = new CoreLocale("special.ptag.prefix", "");
		GROUP_TAG_SUFFIX = new CoreLocale("special.ptag.suffix", "");
		SPECIAL_CHAT_PINGCOLOR = new CoreLocale("special.chatping.color", "&2");
		SPECIAL_CHAT_PINGSYMBAL = new CoreLocale("special.chatping.symbal", "@");
	}

	public CoreLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}
}
