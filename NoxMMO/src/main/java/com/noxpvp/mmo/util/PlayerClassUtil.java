/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.noxpvp.core.utils.UUIDUtil;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.reflection.SafeField;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.collection.DualAccessMap;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.AxesPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;

public class PlayerClassUtil { //TODO: UUID's
	public static final String LOG_MODULE_NAME = "PlayerClass";

	private static final Class<? extends PlayerClass>[] classes = new Class[]{
			AxesPlayerClass.class
	};

	private static ModuleLogger log;

	private static Map<String, PlayerClass> classCache; //FIXME: Double memory possible due to the below comment.
	/*
	 * When the player does not have uuid the cache could reference their name.
	 * When doing so and their uuid is now available. It will now think that they don't exist in cache causing double class object storage.
	 * During this the objects still remain near complete duplicates. There is only a very slight chance of data not being saved during such transition.
	 *
	 * However it will still load data no matter what. So this should be fine. Data is saved quite often anyways Especially on setting a home.
	 * Setting homes forces a save operation anyways.
	 */

	private static DualAccessMap<String, String> classIdNameMap; //Key = classId : value = className
	private static DualAccessMap<String, Class<? extends PlayerClass>> pClasses;    //Used to cache class instances. Used to not generate many class objects.

	private PlayerClassUtil() {
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
	 * This will discard invalid classes and throw {@linkplain Level#SEVERE} log message into console.l
	 *
	 * @param clazz
	 */
	public static void registerPlayerClass(Class<? extends PlayerClass> clazz) {
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
	public static List<PlayerClass> getAvailableClasses(Player player) {
		List<PlayerClass> ret = new ArrayList<PlayerClass>();
		for (PlayerClass c : getAllClasses(player))
			if (c.canUseClass())
				ret.add(c);

//		return ret;
		return getAllClasses(player);
	}

	@Deprecated
	private static List<PlayerClass> getAllClasses(Player player) {
		List<PlayerClass> ret = new ArrayList<PlayerClass>();
		for (Class c : getPClasses()) {
			PlayerClass p = safeConstructClass(c, player);
			if (p != null)
				ret.add(p);
		}

		return ret;
	}

	public static Collection<String> getAllClassNames() {
		return classIdNameMap.valueSet();
	}

	public static String getClassNameById(String id) {
		if (hasClassId(id))
			return classIdNameMap.get(id);
		else
			return null;
	}

	public static String getIdByClassName(String name) {
		if (hasClassName(name))
			return classIdNameMap.getByValue(name);
		else
			return null;
	}

	public static Collection<Class<? extends PlayerClass>> getPClasses() {
		return pClasses.values();
	}

	public static boolean hasClassId(String id) {
		return classIdNameMap.containsKey(id);
	}

	public static boolean hasClassName(String name) {
		return classIdNameMap.containsValue(name);
	}

	public static boolean hasClassNameIgnoreCase(String name) {
		for (String cn : classIdNameMap.values())
			if (cn.equalsIgnoreCase(name))
				return true;

		return false;
	}

	public static String getClassIDbyClass(Class<?> clazz) {
		if (pClasses.containsValue(clazz))
			return pClasses.getByValue(clazz);
		return null;
	}

	private static PlayerClass safeConstructClass(Class clazz, Player player) {
		return safeConstructClass(clazz, UUIDUtil.compressUUID(player.getUniqueId()));
	}

	private static PlayerClass safeConstructClass(Class c, String playerIdentifier) {
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

	public static PlayerClass safeConstructClass(String classId, Player player) {
		if (player == null)
			throw new IllegalArgumentException("Player cannot be null!");
		return safeConstructClass(classId, player.getName());
	}

	public static PlayerClass safeConstructClass(String classId, String playerName) {
		if (!pClasses.containsKey(classId))
			return null;

		Class c = pClasses.get(classId);
		if (c == null)
			return null;

		return safeConstructClass(c, playerName);
	}

	public static void init() {
		log = NoxMMO.getInstance().getModuleLogger(LOG_MODULE_NAME);

		classIdNameMap = new DualAccessMap<String, String>();
		pClasses = new DualAccessMap<String, Class<? extends PlayerClass>>();

		classCache = new MapMaker().weakValues().concurrencyLevel(2).makeMap();

		addDefaults();
	}

	private static void addDefaults() {
		for (Class<? extends PlayerClass> clazz : classes)
			registerPlayerClass(clazz);
	}

	/**
	 * Creates a new class based on serialized data maps.
	 * <br/>
	 * This will also load the data associated with the serialized method.
	 * @param data map of serialized data.
	 */
	public static PlayerClass safeConstructClass(Map<String, Object> data) {
		final String uuid = data.get("class.uuid").toString();
		final String playerIdent = data.get("player-ident").toString();

		PlayerClass ret = safeConstructClass(uuid, playerIdent);
		if (ret != null) ret.onLoad(data);

		return ret;
	}
}
