package com.noxpvp.mmo;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.utils.PermissionHandler;
import com.noxpvp.mmo.abilities.entity.*;
import com.noxpvp.mmo.abilities.player.*;
import com.noxpvp.mmo.abilities.player.AutoToolAbilities.AutoArmor;
import com.noxpvp.mmo.abilities.player.AutoToolAbilities.AutoSword;
import com.noxpvp.mmo.abilities.player.AutoToolAbilities.AutoTool;
import com.noxpvp.mmo.abilities.targeted.*;
import com.noxpvp.mmo.classes.player.main.axes.BasherClass;
import com.noxpvp.mmo.classes.player.main.axes.BerserkerClass;
import com.noxpvp.mmo.classes.player.main.axes.ChampionClass;
import com.noxpvp.mmo.classes.player.main.axes.WarlordClass;
import com.noxpvp.mmo.listeners.BlockListener;
import com.noxpvp.mmo.listeners.DamageListener;
import com.noxpvp.mmo.listeners.ExperienceListener;
import com.noxpvp.mmo.listeners.PacketListeners;
import com.noxpvp.mmo.listeners.PacketListeners.EntityEquipmentListener;
import com.noxpvp.mmo.listeners.PacketListeners.WorldSoundListener;
import com.noxpvp.mmo.listeners.PlayerTargetListener;


public class NoxMMO extends NoxPlugin {
	public static final String PERM_NODE = "nox.mmo";

	private static NoxMMO instance;
	private NoxCore core;
	
	private PermissionHandler permHandler;
	
	DamageListener damageListener;
	PlayerTargetListener playerTargetListener;
	BlockListener blockListener;
	ExperienceListener experieneceListener;
	
	PacketListeners packetListeners;
	EntityEquipmentListener equipmentPacketListener;
	WorldSoundListener worldSoundListener;
	
	private FileConfiguration config;
	private FileConfiguration experience;
	
	private MasterListener masterListener;
	
	private PlayerManager playerManager = null;
	
	@Override
	public boolean command(CommandSender arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disable() {
		masterListener.unregisterAll(); 
		ShurikenAbility.shurikenThrowers = null;
		HammerOfThorAbility.hammerThrowers = null;
		HookShotAbility.hookArrows = null;
		SeveringStrikesAbility.strikers = null;
		permHandler = null;
		masterListener = null;
		setInstance(null);
	}
	
	public FileConfiguration getFileConfig(){
		if (config == null)
			config = new FileConfiguration(this, "config.yml");
		return config;
	}
	
	public FileConfiguration getExperienceConfig(){
		if (experience == null)
			experience = new FileConfiguration(this, "experience.yml");
		return experience;
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
		masterListener = new MasterListener();
		
		playerManager = new PlayerManager();
		core = NoxCore.getInstance();
		
		damageListener = new DamageListener(instance);
		playerTargetListener = new PlayerTargetListener(instance);
		blockListener = new BlockListener(instance);
		experieneceListener = new ExperienceListener(instance);
		
		packetListeners = new PacketListeners();
		equipmentPacketListener = packetListeners.new EntityEquipmentListener();
		worldSoundListener = packetListeners.new WorldSoundListener();
		
		damageListener.register();
		playerTargetListener.register();
		blockListener.register();
		permHandler = new PermissionHandler(this);
		experieneceListener.register();
		
		register(equipmentPacketListener, PacketType.OUT_ENTITY_EQUIPMENT);
		register(worldSoundListener, PacketType.OUT_NAMED_SOUND_EFFECT);
	}

	private void setInstance(NoxMMO noxMMO) {
		instance = noxMMO;
	}
	
	@Override
	public void saveDefaultConfig() {
		//TODO: Add Defaults.
		saveConfig();
	}

	@Override
	public void saveConfig() {
		
		config.save();
	}
	
	@Override
	public void reloadConfig() {
		config.load();
		
	}
	
	@Override
	public void permissions() {
		addPermission(new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability"), "Base MMO Node", PermissionDefault.FALSE,
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", AutoArmor.PERM_NODE), "Allows usage of the Auto Armor Abilities.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", AutoSword.PERM_NODE), "Allows usage of the Auto Sword Abilities.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", AutoTool.PERM_NODE), "Allows usage of the Auto Tool Abilities.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", BackStabAbility.PERM_NODE), "Allows usage of the Back Stab Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", BandageAbility.PERM_NODE), "Allows usage of the Bandage Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", BankShotAbility.PERM_NODE), "Allows usage of the Bank Shot Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", BoltAbility.PERM_NODE), "Allows usage of the Bolt Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", CallOfTheWildAbility.PERM_NODE), "Allows usage of the Call Of The Wild Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", CurseAbility.PERM_NODE), "Allows usage of the Curse Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", DashAbility.PERM_NODE), "Allows usage of the Dash Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", DemoralizingRoarAbility.PERM_NODE), "Allows usage of the Demoralizing Roar Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", DisarmAbility.PERM_NODE), "Allows usage of the Disarm Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ExplosiveArrowAbility.PERM_NODE), "Allows usage of the Explosive Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", FireBallAbility.PERM_NODE), "Allows usage of the Fire Ball Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", FireNovaAbility.PERM_NODE), "Allows usage of the Fire Nova Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ForcePullAbility.PERM_NODE), "Allows usage of the Force Pull Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", GrappleThrowAbility.PERM_NODE), "Allows usage of the Grapple Throw Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", GuardianAngelAbility.PERM_NODE), "Allows usage of the Guardian Angel Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", HammerOfThorAbility.PERM_NODE), "Allows usage of the Hammer Of Thor Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", HitVanishedPlayers.PERM_NODE), "Allows usage of the Hit Vanished Players Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", HookShotAbility.PERM_NODE), "Allows usage of the Hook Shot Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", LeapAbility.PERM_NODE), "Allows usage of the Leap Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MaliciousBiteAbility.PERM_NODE), "Allows usage of the Malicious Bite Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MassDestructionAbility.PERM_NODE), "Allows usage of the Mass Destruction Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MeasuringTapeAbility.PERM_NODE), "Allows usage of the Measuring Tape Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MedPackAbility.PERM_NODE), "Allows usage of the Med-Pack Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MortalWoundAbility.PERM_NODE), "Allows usage of the Mortal Wound Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", NetArrowAbility.PERM_NODE), "Allows usage of the Net Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ParryAbility.PERM_NODE), "Allows usage of the Parry Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", PickPocketAbility.PERM_NODE), "Allows usage of the Pick Pocket Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", PoisonArrowAbility.PERM_NODE), "Allows usage of the Poison Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ReincarnateAbility.PERM_NODE), "Allows usage of the Reincarnation Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", RejuvenationAbility.PERM_NODE), "Allows usage of the Rejuvenation Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SeveringStrikesAbility.PERM_NODE), "Allows usage of the Severing Strikes Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ShadowStepAbility.PERM_NODE), "Allows usage of the Shadow Step Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ShurikenAbility.PERM_NODE), "Allows usage of the Shuriken Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SkullSmasherAbility.PERM_NODE), "Allows usage of the Skull Smasher Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SoulStealAbility.PERM_NODE), "Allows usage of the Soul Steal Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SootheAbility.PERM_NODE), "Allows usage of the Soothe Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SuperBreakerAbility.PERM_NODE), "Allows usage of the Super Breaker Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", TracerArrowAbility.PERM_NODE), "Allows usage of the Tracer Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", TrackingAbility.PERM_NODE), "Allows usage of the Tracking Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", VanishAbility.PERM_NODE), "Allows usage of the Vanish Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", WhistleAbility.PERM_NODE), "Allows usage of the Whistle Ability.", PermissionDefault.OP)
		));
		
		addPermission(new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", "*"), "ALlow access to all classes.", PermissionDefault.OP,
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", BasherClass.className), "Allows access to the class named " + BasherClass.className , PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", BerserkerClass.className), "Allows access to the class named " + BerserkerClass.className, PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", ChampionClass.className), "Allows access to the class named " + ChampionClass.className, PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", WarlordClass.className), "Allows access to the class named " + WarlordClass.className, PermissionDefault.OP)
		));
	}

	@Override
	public void localization() {
		loadLocale("ability.already-active", "&4You cannot use the ability \"%1%\" as it is already active!");
		loadLocale("ability.activated.default", "&a%1% Activated!");
		
		//Ability - MedPack
		loadLocale("ability.medpack.use", "&eMedPack dropped!");
		loadLocale("ability.medpack.picked-up.other", "&c%1%&e Picked up your dropped medpack!");
		loadLocale("ability.medpack.picked-up", "&ePicked up %2%'s dropped medpack!");
		
		//Ability - NetArrow 
		loadLocale("ability.arrow.net.use", getLocale("ability.activated", NetArrowAbility.ABILITY_NAME)); //Dynamic defaults FTW
		loadLocale("ability.arrow.net.trapped", "&cSomething was caught in the net!"); //%1% should be a list comma delimited of entities or players. Default does not contain it.
		
		//Ability - Explosive Arrow
		loadLocale("ability.arrow.explosive", "&eExplosive Arrow Activated!"); //FIXME: Unused
		
		//Ability - 
		
	}

	public MasterListener getMasterListener(){
		return masterListener;
	}
	
	/**
	 * Gets the player manager.
	 *
	 * @return the player manager
	 */
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	@Override
	public NoxCore getCore() {
		return core;
	}

	@Override
	public PermissionHandler getPermissionHandler() {
		return permHandler;
	}
	
	public static NoxMMO getInstance() { return instance; }
	
}
