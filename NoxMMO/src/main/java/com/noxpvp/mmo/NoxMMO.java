package com.noxpvp.mmo;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.mmo.abilities.entity.*;
import com.noxpvp.mmo.abilities.player.*;
import com.noxpvp.mmo.abilities.targeted.*;


public class NoxMMO extends NoxPlugin {
	public static final String PERM_NODE = "nox.mmo";

	private static NoxMMO instance;
	private NoxCore core;
	
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
		core = NoxCore.getInstance();
	}
	
	private void setInstance(NoxMMO noxMMO) {
		instance = noxMMO;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void permissions() {
		addPermission(new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability"), "Base MMO Node", PermissionDefault.FALSE,
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", BackStabAbility.PERM_NODE), "Allows usage of the Back Stab Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", BandageAbility.PERM_NODE), "Allows usage of the Bandage Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", BankShotAbility.PERM_NODE), "Allows usage of the Bank Shot Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", BoltAbility.PERM_NODE), "Allows usage of the Bolt Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", CallOfTheWildAbility.PERM_NODE), "Allows usage of the Call Of The Wild Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", CurseAbility.PERM_NODE), "Allows usage of the Curse Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", DashAbility.PERM_NODE), "Allows usage of the Dash Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", DemoralizingRoarAbility.PERM_NODE), "Allows usage of the Demoralizing Roar Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", DisarmAbility.PERM_NODE), "Allows usage of the Disarm Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", ExplosiveArrowAbility.PERM_NODE), "Allows usage of the Explosive Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", FireBallAbility.PERM_NODE), "Allows usage of the Fire Ball Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", FireNovaAbility.PERM_NODE), "Allows usage of the Fire Nova Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", ForcePullAbility.PERM_NODE), "Allows usage of the Force Pull Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", GrappleThrowAbility.PERM_NODE), "Allows usage of the Grapple Throw Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", GuardianAngelAbility.PERM_NODE), "Allows usage of the Guardian Angel Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", HammerOfThorAbility.PERM_NODE), "Allows usage of the Hammer Of Thor Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", HitVanishedPlayers.PERM_NODE), "Allows usage of the Hit Vanished Players Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", HookShotAbility.PERM_NODE), "Allows usage of the Hook Shot Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", LeapAbility.PERM_NODE), "Allows usage of the Leap Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", MaliciousBiteAbility.PERM_NODE), "Allows usage of the Malicious Bite Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", MassDestructionAbility.PERM_NODE), "Allows usage of the Mass Destruction Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", MeasuringTapeAbility.PERM_NODE), "Allows usage of the Measuring Tape Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", MedPackAbility.PERM_NODE), "Allows usage of the Med-Pack Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", MortalWoundAbility.PERM_NODE), "Allows usage of the Mortal Wound Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", NetArrowAbility.PERM_NODE), "Allows usage of the Net Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", ParryAbility.PERM_NODE), "Allows usage of the Parry Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", PickPocketAbility.PERM_NODE), "Allows usage of the Pick Pocket Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", PoisonArrowAbility.PERM_NODE), "Allows usage of the Poison Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", ReincarnateAbility.PERM_NODE), "Allows usage of the Reincarnation Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", RejuvenationAbility.PERM_NODE), "Allows usage of the Rejuvenation Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", SeveringStrikesAbility.PERM_NODE), "Allows usage of the Severing Strikes Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", ShadowStepAbility.PERM_NODE), "Allows usage of the Shadow Step Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", ShurikenAbility.PERM_NODE), "Allows usage of the Shuriken Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", SkullSmasherAbility.PERM_NODE), "Allows usage of the Skull Smasher Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", SoulStealAbility.PERM_NODE), "Allows usage of the Soul Steal Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", SootheAbility.PERM_NODE), "Allows usage of the Soothe Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", SuperBreakerAbility.PERM_NODE), "Allows usage of the Super Breaker Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", TracerArrowAbility.PERM_NODE), "Allows usage of the Tracer Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", TrackingAbility.PERM_NODE), "Allows usage of the Tracking Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", VanishAbility.PERM_NODE), "Allows usage of the Vanish Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.combine(".", PERM_NODE, "ability", WhistleAbility.PERM_NODE), "Allows usage of the Whistle Ability.", PermissionDefault.OP)
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

	@Override
	public NoxCore getCore() {
		return core;
	} 
}
