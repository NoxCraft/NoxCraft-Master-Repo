package com.noxpvp.mmo;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.mmo.abilities.entity.LeapAbility;
import com.noxpvp.mmo.abilities.player.HammerOfThorAbility;
import com.noxpvp.mmo.abilities.player.MassDestructionAbility;
import com.noxpvp.mmo.abilities.player.ShurikenAbility;

public class NoxMMO extends NoxPlugin {
	public static final String PERM_NODE = "nox.mmo";

	private static NoxMMO instance;
	
	private PlayerManager playerManager = null;
	
	@Override
	public boolean command(CommandSender arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disable() {
		MassDestructionAbility.massDestructors = null;
		ShurikenAbility.shurikenThrowers = null;
		HammerOfThorAbility.hammerThrowers = null;
		
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
		
		playerManager = new PlayerManager();
	}
	
	private void setInstance(NoxMMO noxMMO) {
		instance = noxMMO;
	}
	
	@Override
	public void permissions() {
		addPermission(new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability"), "Base MMO Node", PermissionDefault.FALSE, 
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", LeapAbility.PERM_NODE), "Allows usage of the leap ability.", PermissionDefault.OP )
		));
	}

	/**
	 * Gets the player manager.
	 *
	 * @return the player manager
	 */
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public static NoxMMO getInstance() { return instance; } 
}
