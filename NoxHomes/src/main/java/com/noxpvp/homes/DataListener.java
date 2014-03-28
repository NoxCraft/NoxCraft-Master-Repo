package com.noxpvp.homes;

import java.util.logging.Level;

import com.noxpvp.core.listeners.NoxListener;

public class DataListener extends NoxListener<NoxHomes> {

	public DataListener(NoxHomes plugin) {
		super(plugin);
		plugin.log(Level.WARNING, "Data listener was made when it does nothing...");
	}
}
