package com.noxpvp.core;

import com.noxpvp.core.reloader.BaseReloader;


public final class MasterReloader extends BaseReloader {
	private static final String root = "Master";
	private static MasterReloader instance;

	private MasterReloader() {
		super(null, root);
	}

	public static MasterReloader getInstance() {
		if (instance == null)
			instance = new MasterReloader();

		return instance;
	}

	/**
	 * @deprecated Method does nothing.
	 * <br/><br/>
	 * <p/>
	 * Must specify a module in {@link #reload(String)} instead or use {@link #reloadAll()} to reload all modules.
	 * <br/><br/>
	 * <p/>
	 * <b> Warning using {@link #reloadAll()} May be intensive due to fact any plugin hooked into this will reload its configs all at once.</b>
	 */
	public boolean reload() {
		return false;
	}

}
