package com.noxpvp.mmo.util;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.reflection.SafeField;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.collection.DualAccessMap;
import com.noxpvp.core.utils.UUIDUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.AxesPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PlayerClassUtil {

	public static final String LOG_MODULE_NAME = "PlayerClass";

	private static ModuleLogger log;

	private static PlayerClassConstructUtil constructUtil;

	private static Map<String, PlayerClass> classCache; //FIXME: Double memory possible due to the below comment.

	public static void init() {
		constructUtil = new PlayerClassConstructUtil();

	}

	/**
	 * Retrieves all player classes associated with the player.
	 *
	 * This will include any classes they do not have access to but have data stored in them.
	 *
	 * @see #getAllPlayerClasses(MMOPlayer)
	 *
	 * @param player
	 * @return List of classes
	 */
	public static List<PlayerClass> getAllPlayerClasses(OfflinePlayer player) {
		return getAllPlayerClasses(MMOPlayerManager.getInstance().getPlayer(player));
	}

	/**
	 * Retrieves all player classes associated with the player.
	 *
	 * This will include any classes they do not have access to but have data stored in them.
	 *
	 * @param player MMOPlayer object.
	 * @return List of classes or null if (player == null)
	 */
	public static List<PlayerClass> getAllPlayerClasses(MMOPlayer player) {
		if (player == null) return null;
		List<PlayerClass> ret = player.getClasses(); //Can be empty if they don't have any classes.

		//FIXME: IMPLEMENT

		return ret;
	}

	/**
	 * Retrieves all usable classes associated with the player.
	 *
	 * This will not include any classes they cannot access. Even if data is present for it.
	 *
	 * @see #getAllowedPlayerClasses(com.noxpvp.mmo.MMOPlayer)
	 *
	 * @param player the player to grab the classes from.
	 * @return List of classes or null if (player == null)
	 */
	public static List<PlayerClass> getAllowedPlayerClasses(OfflinePlayer player) {
		return getAllowedPlayerClasses(MMOPlayerManager.getInstance().getPlayer(player));
	}

	/**
	 * Retrieves all usable classes associated with the player.
	 *
	 * This will not include any classes they cannot access. Even if data is present for it.
	 *
	 * @see #filterAllowedClasses(java.util.List)
	 *
	 * @param player the player to grab the classes from.
	 * @return List of classes or null if (player == null)
	 */
	public static List<PlayerClass> getAllowedPlayerClasses(MMOPlayer player) {
		if (player == null) return null;
		List<PlayerClass> ret = getAllPlayerClasses(player);

		filterAllowedClasses(ret);

		return ret;
	}

	/**
	 * Filters the classes to only allow anything that the player has permission to use to stay on the list.
	 * @param classes a valid modifiable list of player classes to edit.
	 */
	public static void filterAllowedClasses(List<PlayerClass> classes) {
		//FIXME: Implement.
	}

	/**
	 * Filters the classes to only allow anything that has data other than default.
	 * @param classes a valid modifiable list of player classes to edit.
	 */
	public static void filterChangedClasses(List<PlayerClass> classes) {
		//FIXME: Implement.
	}

	/**
	 * Retrieves all classes that may be saved to data.
	 *
	 * @see #getAllChangedPlayerClasses(com.noxpvp.mmo.MMOPlayer)
	 *
	 * @param player player to grab classes from
	 * @return List of player class objects.
	 */
	public static List<PlayerClass> getAllChangedPlayerClasses(OfflinePlayer player) {
		return getAllChangedPlayerClasses(MMOPlayerManager.getInstance().getPlayer(player));
	}

	/**
	 * Retrieves all classes that may be saved to data.
	 *
	 * @see #filterChangedClasses(java.util.List)
	 *
	 * @param player player to grab classes from
	 * @return List of player class objects.
	 */
	public static List<PlayerClass> getAllChangedPlayerClasses(MMOPlayer player) {
		List<PlayerClass> ret = getAllPlayerClasses(player);

		filterChangedClasses(ret);

		return ret;
	}

	/**
	 * Classes must have the following constructors. (Each number represent a constructor with the following params.
	 * Bold signifies that internal mechanisms depend heavily on and <b>MUST</b> be implemented.
	 * <ol>
	 * <li><b>(String playerName)</b></li>
	 * </ol>
	 * <br/><br/>
	 * You must implement the following:<br/>
	 * <ul>
	 * <li> public static final String variable of "uniqueID";</li>
	 * <li> public static final String variable of "className";</li>
	 * </ul>
	 * <p/>
	 * <p/>
	 * This will discard invalid classes and throw {@linkplain java.util.logging.Level#SEVERE} log message into console.l
	 *
	 * @see com.noxpvp.mmo.util.PlayerClassUtil.PlayerClassConstructUtil#registerPlayerClass(Class)
	 *
	 * @param clazz class for registration
	 */
	public static void registerPlayerClass(Class<? extends PlayerClass> clazz) {
		constructUtil.registerPlayerClass(clazz);
	}

	/**
	 * Internal methods to deal with class initialization on a dynamic bases.
	 *
	 * <p>These implementations should not be hooked into directly unless you know the ins and outs of how the class system works.</p>
	 *
	 * <!--
	 *      <p>This includes the original creator it.</p>
	 * -->
	 */
	protected static class PlayerClassConstructUtil { //TODO: UUID's

		public PlayerClassConstructUtil() {
			log = NoxMMO.getInstance().getModuleLogger(LOG_MODULE_NAME);

			classIdNameMap = new DualAccessMap<String, String>();
			pClasses = new DualAccessMap<String, Class<? extends PlayerClass>>();

			classCache = new MapMaker().weakValues().concurrencyLevel(2).makeMap();

			addDefaults();
		}

		private final Class<? extends PlayerClass>[] classes = new Class[]{
				AxesPlayerClass.class
		};

		/*
		 * When the player does not have uuid the cache could reference their name.
		 * When doing so and their uuid is now available. It will now think that they don't exist in cache causing double class object storage.
		 * During this the objects still remain near complete duplicates. There is only a very slight chance of data not being saved during such transition.
		 *
		 * However it will still load data no matter what. So this should be fine. Data is saved quite often anyways Especially on setting a home.
		 * Setting homes forces a save operation anyways.
		 */
		private DualAccessMap<String, String> classIdNameMap; //Key = classId : value = className

		private DualAccessMap<String, Class<? extends PlayerClass>> pClasses;    //Used to cache class instances. Used to not generate many class objects.

		/**
		 * Classes must have the following constructors. (Each number represent a constructor with the following params.
		 * Bold signifies that internal mechanisms depend heavily on and <b>MUST</b> be implemented.
		 * <ol>
		 * <li><b>(String playerName)</b></li>
		 * </ol>
		 * <br/><br/>
		 * You must implement the following:<br/>
		 * <ul>
		 * <li> public static final String variable of "uniqueID";</li>
		 * <li> public static final String variable of "className";</li>
		 * </ul>
		 * <p/>
		 * <p/>
		 * This will discard invalid classes and throw {@linkplain java.util.logging.Level#SEVERE} log message into console.l
		 *
		 * @param clazz for registration
		 */
		public void registerPlayerClass(Class<? extends PlayerClass> clazz) {
			SafeField<String> className = new SafeField<String>(clazz, "className");
			SafeField<String> classId = new SafeField<String>(clazz, "uniqueID");

			boolean bad = false;
			if (!classId.isValid() || !classId.isStatic()) {
				log.severe("PlayerClass \"" + clazz.getName() + "\" is not valid. It does not have a static String of className");
				bad = true;
			}

			if (!className.isValid() || !className.isStatic()) {
				log.severe("PlayerClass \"" + clazz.getName() + "\" is not valid. It does not have a static String of uniqueID");
				bad = true;
			}

			if (bad)
				return;

			String cName = className.get(clazz), cId = classId.get(clazz);

			pClasses.put(cId, clazz);
			classIdNameMap.put(cId, cName);

			NoxMMO mmo = NoxMMO.getInstance();

			log.fine("Registered the class \"" + cName + "\" with id \"" + cId + "\".");
			mmo.loadLocale(MMOLocale.CLASS_DISPLAY_NAME.getName() + "." + cName, cName);

			mmo.saveLocalization();

		}

		@Deprecated
		public List<PlayerClass> getAvailableClasses(Player player) {
			List<PlayerClass> ret = new ArrayList<PlayerClass>();
			for (PlayerClass c : getAllClasses(player))
				if (c.canUseClass())
					ret.add(c);

			//		return ret;
			return getAllClasses(player);
		}

		@Deprecated
		private List<PlayerClass> getAllClasses(Player player) {
			List<PlayerClass> ret = new ArrayList<PlayerClass>();
			for (Class c : getPClasses()) {
				PlayerClass p = safeConstructClass(c, player);
				if (p != null)
					ret.add(p);
			}

			return ret;
		}

		public Collection<String> getAllClassNames() {
			return classIdNameMap.valueSet();
		}

		public String getClassNameById(String id) {
			if (hasClassId(id))
				return classIdNameMap.get(id);
			else
				return null;
		}

		public String getIdByClassName(String name) {
			if (hasClassName(name))
				return classIdNameMap.getByValue(name);
			else
				return null;
		}

		public Collection<Class<? extends PlayerClass>> getPClasses() {
			return pClasses.values();
		}

		public boolean hasClassId(String id) {
			return classIdNameMap.containsKey(id);
		}

		public boolean hasClassName(String name) {
			return classIdNameMap.containsValue(name);
		}

		public boolean hasClassNameIgnoreCase(String name) {
			for (String cn : classIdNameMap.values())
				if (cn.equalsIgnoreCase(name))
					return true;

			return false;
		}

		public String getClassIDbyClass(Class<?> clazz) {
			if (pClasses.containsValue(clazz))
				return pClasses.getByValue(clazz);
			return null;
		}

		private PlayerClass safeConstructClass(Class clazz, Player player) {
			return safeConstructClass(clazz, UUIDUtil.compressUUID(player.getUniqueId()));
		}

		private PlayerClass safeConstructClass(Class c, String playerIdentifier) {
			String classID = getClassIDbyClass(c);

			if (classID != null) {
				String match = playerIdentifier + "|" + classID;
				if (classCache.containsKey(match) && classCache.get(match) != null)
					return classCache.get(match);
			}

			SafeConstructor sc = new SafeConstructor(c, String.class);

			if (!sc.isValid())
				return null;

			Object o = sc.newInstance(playerIdentifier);

			if (!(o instanceof PlayerClass))
				return null;

			PlayerClass ret = (PlayerClass) o;

			classID = ret.getUniqueID();
			String cs = playerIdentifier + "|" + classID;
			if (LogicUtil.nullOrEmpty(classID))
				log.warning("ClassID for class " + ret.getName() + " is null or empty!");
			else
				classCache.put(cs, ret);

			return ret;
		}

		public PlayerClass safeConstructClass(String classId, Player player) {
			if (player == null)
				throw new IllegalArgumentException("Player cannot be null!");
			return safeConstructClass(classId, player.getName());
		}

		public PlayerClass safeConstructClass(String classId, String playerName) {
			if (!pClasses.containsKey(classId))
				return null;

			Class c = pClasses.get(classId);
			if (c == null)
				return null;

			return safeConstructClass(c, playerName);
		}

		private void addDefaults() {
			for (Class<? extends PlayerClass> clazz : classes)
				registerPlayerClass(clazz);
		}

		/**
		 * Creates a new class based on serialized data maps.
		 * <br/>
		 * This will also load the data associated with the serialized method.
		 * @param data map of serialized data.
		 */
		public PlayerClass safeConstructClass(Map<String, Object> data) {
			final String uuid = data.get("class.uuid").toString();
			final String playerIdent = data.get("player-ident").toString();

			PlayerClass ret = safeConstructClass(uuid, playerIdent);
			if (ret != null) ret.onLoad(data);

			return ret;
		}
	}

}
