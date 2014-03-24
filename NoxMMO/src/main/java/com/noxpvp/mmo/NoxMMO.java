package com.noxpvp.mmo;

import java.util.logging.Level;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.Command;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.*;
import com.noxpvp.core.utils.StaticCleaner;
import com.noxpvp.mmo.abilities.entity.*;
import com.noxpvp.mmo.abilities.player.*;
import com.noxpvp.mmo.abilities.player.AutoToolAbilities.*;
import com.noxpvp.mmo.abilities.targeted.*;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.command.ClassCommand;
import com.noxpvp.mmo.listeners.*;
import com.noxpvp.mmo.listeners.PacketListeners.*;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.util.PlayerClassUtil;


public class NoxMMO extends NoxPlugin {
	public static final String PERM_NODE = "nox.mmo";

	private static NoxMMO instance;
	private NoxCore core;
	
	private PermissionHandler permHandler;
	
	DamageListener damageListener;
	PlayerInteractListener playerTargetListener;
	BlockListener blockListener;
	ExperienceListener experieneceListener;
	
	PacketListeners packetListeners;
//	PlayerAnimationListener playerAnimationListener;
	EntityEquipmentListener equipmentPacketListener;
	WorldSoundListener worldSoundListener;
	
	private FileConfiguration config;
	private FileConfiguration experience;
	
	private MasterListener masterListener;
	
	private PlayerManager playerManager = null;

	private Class<Command>[] commands =  (Class<Command>[]) new Class[]{ ClassCommand.class };
	
	@Override
	public void disable() {
		masterListener.unregisterAll(); 
		permHandler = null;
		masterListener = null;
		
		Class<?>[] classes = {
				ShurikenAbility.class, HammerOfThorAbility.class,
				HookShotAbility.class, SeveringStrikesAbility.class,
				PlayerClass.class, PlayerClassUtil.class,
				AbilityCycler.class, MasterListener.class
		};
		
		String[] internals = { };
		
		new StaticCleaner(this, getClassLoader(), internals, classes).resetAll();
		
		setInstance(null);
	}
	
	public FileConfiguration getMMOConfig(){
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
		MasterListener.init();
		masterListener = new MasterListener();
		
		
		getPlayerManager();
		
		core = NoxCore.getInstance();
		
		PlayerClassUtil.init();
		PlayerClass.init();
		
		damageListener = new DamageListener(instance);
		playerTargetListener = new PlayerInteractListener(instance);
		blockListener = new BlockListener(instance);
		experieneceListener = new ExperienceListener(instance);
		
		packetListeners = new PacketListeners();
//		playerAnimationListener = packetListeners.new PlayerAnimationListener();
		equipmentPacketListener = packetListeners.new EntityEquipmentListener();
		worldSoundListener = packetListeners.new WorldSoundListener();
		
		damageListener.register();
		playerTargetListener.register();
		blockListener.register();
		permHandler = new PermissionHandler(this);
//		experieneceListener.register();
		
//		register(playerAnimationListener, PacketType.IN_ENTITY_ANIMATION);
		register(equipmentPacketListener, PacketType.OUT_ENTITY_EQUIPMENT);
		register(worldSoundListener, PacketType.OUT_NAMED_SOUND_EFFECT);
		
		Reloader base = new BaseReloader(getMasterReloader(), "NoxMMO") {
			public boolean reload() {
				return true;
			}
		};
		
		base.addModule(new BaseReloader(base, "config.yml") {
			
			public boolean reload() {
				reloadConfig();
				return true;
			}
		});
		
		base.addModule(new BaseReloader(base, "locale") {
			
			public boolean reload() {
				localization();
				return true;
			}
		});
		
		base.addModule(new BaseReloader(base, "experience.yml") {
			
			public boolean reload() {
				getExperienceConfig().load();
				return true;
			}
		});
		
		registerAllCommands();
	}
	
	private void registerAllCommands() {
		for (Class<Command> cls : commands)
		{
			SafeConstructor<Command> cons = new SafeConstructor<Command>(cls, new Class[0]);
			Command rn = cons.newInstance();
			if (rn != null)
				registerCommand(rn);
		}
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
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SilentWalkingAbility.PERM_NODE), "Allows usage of the SilentWalking Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SkullSmasherAbility.PERM_NODE), "Allows usage of the Skull Smasher Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SoulStealAbility.PERM_NODE), "Allows usage of the Soul Steal Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SootheAbility.PERM_NODE), "Allows usage of the Soothe Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SuperBreakerAbility.PERM_NODE), "Allows usage of the Super Breaker Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", TracerArrowAbility.PERM_NODE), "Allows usage of the Tracer Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", TrackingAbility.PERM_NODE), "Allows usage of the Tracking Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", VanishAbility.PERM_NODE), "Allows usage of the Vanish Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", WhistleAbility.PERM_NODE), "Allows usage of the Whistle Ability.", PermissionDefault.OP)
		));
		
//		addPermission(new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", "*"), "ALlow access to all classes.", PermissionDefault.OP,
//				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", BasherClass.className), "Allows access to the class named " + BasherClass.className , PermissionDefault.OP),
//				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", BerserkerClass.className), "Allows access to the class named " + BerserkerClass.className, PermissionDefault.OP),
//				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", ChampionClass.className), "Allows access to the class named " + ChampionClass.className, PermissionDefault.OP),
//				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", WarlordClass.className), "Allows access to the class named " + WarlordClass.className, PermissionDefault.OP)
//		));
	}

	@Override
	public void localization() {
		
		loadLocales(MMOLocale.class);
		
//		
//		loadLocale("ability.already-active", "&4You cannot use the ability \"%1%\" as it is already active!");
//		loadLocale("ability.activated.default", "&a%1% Activated!");
//		
//		//Ability - MedPack
//		loadLocale("ability.medpack.use", "&eMedPack dropped!");
//		loadLocale("ability.medpack.picked-up.other", "&c%1%&e Picked up your dropped medpack!");
//		loadLocale("ability.medpack.picked-up", "&ePicked up %2%'s dropped medpack!");
//		
//		//Ability - NetArrow 
//		loadLocale("ability.arrow.net.use", getLocale("ability.activated", NetArrowAbility.ABILITY_NAME)); //Dynamic defaults FTW
//		loadLocale("ability.arrow.net.trapped", "&cSomething was caught in the net!"); //%1% should be a list comma delimited of entities or players. Default does not contain it.
//		
//		//Ability - Explosive Arrow
//		loadLocale("ability.arrow.explosive", "&eExplosive Arrow Activated!"); //FIXME: Unused
//		
		//Ability - 
		
	}

	public MasterListener getMasterListener(){
		return masterListener;
	}
	
	/**
	 * Gets the player manager.
	 *
	 * @Deprecated Use {@link PlayerManager#getInstance()} instead
	 * @return the player manager
	 */
	public PlayerManager getPlayerManager() {
		PlayerManager c = PlayerManager.getInstance();
		if (playerManager == null)
			playerManager = c;
		else if (playerManager != c)
			playerManager = c;
		
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

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends ConfigurationSerializable>[] getSerialiables() {
		return new Class[0];
	}
	
}
