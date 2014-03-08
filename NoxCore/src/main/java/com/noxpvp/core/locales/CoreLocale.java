package com.noxpvp.core.locales;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;

public class CoreLocale extends NoxLocale {

	public final static CoreLocale GROUP_TAG_PREFIX;
	public final static CoreLocale GROUP_TAG_SUFFIX;
	public final static CoreLocale SPECIAL_CHAT_PINGCOLOR;
	public final static CoreLocale SPECIAL_CHAT_PINGSYMBAL;
	public final static CoreLocale TAB_VIEW_HEADER;
	public final static CoreLocale TAB_VIEW_UNDERLINE;
	
	static {
		GROUP_TAG_PREFIX = new CoreLocale("special.ptag.prefix", "");
		GROUP_TAG_SUFFIX = new CoreLocale("special.ptag.suffix", "");
		SPECIAL_CHAT_PINGCOLOR = new CoreLocale("special.chatping.color", "&2");
		SPECIAL_CHAT_PINGSYMBAL = new CoreLocale("special.chatping.symbal", "@");
		TAB_VIEW_HEADER = new CoreLocale("special.tabview.header", "&2   Welcome to\n&cNox Imperialis!");
		TAB_VIEW_UNDERLINE = new CoreLocale("special.tabview.underline", "&6[====_~x~_====\n====_*~*_====\n====_~x~_====]");
	}
	
	public CoreLocale(String name, String defValue) {
		super(name, defValue);
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}
}
