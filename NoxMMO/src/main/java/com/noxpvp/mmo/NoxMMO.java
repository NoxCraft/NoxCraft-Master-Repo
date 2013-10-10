package com.noxpvp.mmo;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.noxpvp.core.NoxPlugin;

public class NoxMMO extends NoxPlugin {
	private static NoxMMO instance;
	
	private PlayerAbilityManager abilityManager = null;
	
	@Override
	public boolean command(CommandSender arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disable() {
		setInstance(null);
	}

	@Override
	public void enable() {
		if (instance != null)
		{
			log(Level.SEVERE, "This plugin already has an instance running!! Disabling second run.");
			setEnabled(false);
			return;
		}
		
		setInstance(this);
	}
	
	private void setInstance(NoxMMO noxMMO) {
		instance = noxMMO;
	}

	/**
	 * Gets the ability manager.
	 *
	 * @return the ability manager
	 */
	public PlayerAbilityManager getAbilityManager() {
		return abilityManager;
	}
	
	public static NoxMMO getInstance() { return instance; } 
}
