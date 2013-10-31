package com.noxpvp.mmo;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.mmo.abilities.entity.DemoralizingRoarAbility;
import com.noxpvp.mmo.abilities.entity.LeapAbility;
import com.noxpvp.mmo.abilities.player.BackStabAbility;
import com.noxpvp.mmo.abilities.player.BankShotAbility;
import com.noxpvp.mmo.abilities.player.DisarmAbility;
import com.noxpvp.mmo.abilities.player.FireBallAbility;
import com.noxpvp.mmo.abilities.player.GuardianAngelAbility;
import com.noxpvp.mmo.abilities.player.HitVanishedPlayers;
import com.noxpvp.mmo.abilities.player.HookShotAbility;
import com.noxpvp.mmo.abilities.player.MassDestructionAbility;
import com.noxpvp.mmo.abilities.player.PickPocketAbility;
import com.noxpvp.mmo.abilities.player.RejuvenationAbility;
import com.noxpvp.mmo.abilities.player.ShadowStepAbility;
import com.noxpvp.mmo.abilities.player.SoulStealAbility;
import com.noxpvp.mmo.abilities.player.SuperBreakerAbility;
import com.noxpvp.mmo.abilities.player.TrackingAbility;

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
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", BackStabAbility.PERM_NODE), "Allows usage of the Back Stab Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", LeapAbility.PERM_NODE), "Allows usage of the leap ability.", PermissionDefault.OP ),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", BankShotAbility.PERM_NODE), "Allows usage of the Bank Shot Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", DisarmAbility.PERM_NODE), "Allows usage of the Disarm Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", DemoralizingRoarAbility.PERM_NODE), "Allows usage of the Demoralizing Road Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", FireBallAbility.PERM_NODE), "Allows usage of the Fire Ball Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", GuardianAngelAbility.PERM_NODE), "Allows usage of the Guardian Angel Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", HitVanishedPlayers.PERM_NODE), "Allows user to Hit Vanished Players.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", HookShotAbility.PERM_NODE), "Allows usage of the Hook Shot Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", MassDestructionAbility.PERM_NODE), "Allows usage of the Mass Destruction Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", PickPocketAbility.PERM_NODE), "Allows usage of the Pick Pocket Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", RejuvenationAbility.PERM_NODE), "Allows usage of the Rejuvenation Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", ShadowStepAbility.PERM_NODE), "Allows usage of the Shadow Step Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", SoulStealAbility.PERM_NODE), "Allows usage of the Soul Steal Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", SuperBreakerAbility.PERM_NODE), "Allows usage of the Super Breaker Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", TrackingAbility.PERM_NODE), "Allows usage of the Tracking Ability.", PermissionDefault.OP)
				)
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
