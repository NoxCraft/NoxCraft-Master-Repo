package com.noxpvp.mmo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.reflection.SafeField;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.collection.DualAccessMap;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;

public class PlayerClassUtil { //TODO: UUID's
	public static final String LOG_MODULE_NAME = "PlayerClass";
	private static ModuleLogger log;
	
	private static Map<String, PlayerClass> classCache;
	
	private static DualAccessMap<String, String> classIdNameMap; //Key = classId : value = className
	private static DualAccessMap<String, Class<? extends PlayerClass>> pClasses;	//Used to cache class instances. Used to not generate many class objects.
	
	private PlayerClassUtil() {	}
	
	/**
	 *  Classes must have the following constructors. (Each number represent a constructor with the following params.
	 * Bold signifies that internal mechanisms depend heavily on and <b>MUST</b> be implemented.
	 * <ol>
	 * 	<li><b>(String playerName)</b></li>
	 * </ol>
	 * <br/><br/>
	 * You must implement the following:<br/>
	 * <ul>
	 * 	<li> public static final String variable of "uniqueID";</li>
	 * 	<li> public static final String variable of "className";</li>
	 * </ul>
	 * 
	 * <p>
	 * This will discard invalid classes and throw {@linkplain Level#SEVERE} log message into console.l
	 * @param clazz
	 */
	public static void registerPlayerClass(Class<? extends PlayerClass> clazz) {
		SafeField<String> className = new SafeField<String>(clazz, "className");
		SafeField<String> classId = new SafeField<String>(clazz, "uniqueID");
		
		boolean good = true;
		if (!classId.isValid() || !classId.isStatic()) {
			log.severe("PlayerClass \"" + clazz.getName() + "\" is not valid. It does not have a static String of className");
			good = false;
		}
		
		if (!className.isValid() || !className.isStatic()) {
			log.severe("PlayerClass \"" + clazz.getName() + "\" is not valid. It does not have a static String of uniqueID");
			good = false;
		}
		
		if (!good)
			return;
		
		String cName = className.get(null), cId = classId.get(null);
		
		pClasses.put(cId, clazz);
		classIdNameMap.put(cId, cName);

		NoxMMO mmo = NoxMMO.getInstance();
		
		ConfigurationNode n = mmo.getLocalizationNode("display").getParent();
		
		FileConfiguration f = (FileConfiguration) n;
		
		mmo.loadLocale(MMOLocale.CLASS_DISPLAY_NAME.getName() + "." + cName, cName);
		
		f.save();
		
		log.fine("Registered the class \"" + cName + "\" with id \"" + cId + "\".");
	}
	
	public static List<PlayerClass> getAvailableClasses(Player player)
	{
		List<PlayerClass> ret = new ArrayList<PlayerClass>();
		for (PlayerClass c : getAllClasses(player))
			if (c.canUseClass())
				ret.add(c);
		
		return ret;
	}
	
	private static List<PlayerClass> getAllClasses(Player player) {
		List<PlayerClass> ret = new ArrayList<PlayerClass>();
		for (Class c : getPClasses())
		{
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
	
	public static String getClassIDbyClass(Class<?> clazz)
	{
		if (pClasses.containsValue(clazz))
			return pClasses.getByValue(clazz);
		return null;
	}
	
	private static PlayerClass safeConstructClass(Class clazz, Player player) {
		
		SafeConstructor sc = new SafeConstructor(clazz, Player.class);
		if (!sc.isValid())
			return null;
		
		Object o = sc.newInstance(player);
		if (!(o instanceof PlayerClass))
			return null;

		return (PlayerClass) o;
	}

	private static PlayerClass safeConstructClass(Class c, String playerName) {
		String classID = getClassIDbyClass(c);
		
		if (classID != null)
		{
			String match = playerName + "|" + classID;
			if (classCache.containsKey(match) && classCache.get(match) != null)
				return classCache.get(match);
		}
			
		
		SafeConstructor sc = new SafeConstructor(c, String.class);
		
		if (!sc.isValid())
			return null;
		
		Object o = sc.newInstance(playerName);
		
		if (!(o instanceof PlayerClass))
			return null;
		PlayerClass ret = (PlayerClass)o;
		
		classID = ret.getUniqueID();
		String cs = playerName + "|" + classID;
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
	}
}
