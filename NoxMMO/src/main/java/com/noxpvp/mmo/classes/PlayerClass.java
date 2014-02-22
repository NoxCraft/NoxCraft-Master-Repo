package com.noxpvp.mmo.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.meta.When;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.mmo.NoxMMO;

/**
 * When Implementing you must use all of the following constructors and params.
 * <br/>
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
 */
public abstract class PlayerClass implements IPlayerClass {
	@SuppressWarnings("rawtypes")
	private static List<Class> registeredClasses;
	public static final String LOG_MODULE_NAME = "PlayerClass";
	
	//Debug and errors
	protected static ModuleLogger pcLog;
	protected ModuleLogger log;

	//Identification
	private final String uid;
	private String name;
	
	//Tiers
	private Map<Integer, IClassTier> tiers;
	private int cTierLevel = 1;
	
	//Player Data
	private String playerName;
	
	//Colors
	// IMPLEMENT THIS STATICALLY IN CLASS FILE. //THIS IS NOT GOING TO BE IN HERE BBC....
//	private static Color armourColor;
//	private static ChatColor colorChar;
	
	public PlayerClass(String uid, @Nonnull String name, @Nonnull String playerName) {
		this(uid, name, playerName, null);
	}
	
	public PlayerClass(String uid, @Nonnull String name, @Nonnull Player player) {
		this(uid, name, null, player);
	}
	
	public PlayerClass(String uid, @Nonnull String name, @Nonnull(when = When.MAYBE) String playerName,  @Nonnull(when = When.MAYBE) Player player)
	{
		Validate.notNull(name, "The name of class must not be null!");
		Validate.isTrue((player != null || playerName != null), "Either the player or the playerName must not be null!");
		
		this.uid = uid;
		this.name = name;
		
		if (playerName == null)
			this.playerName = player.getName();
		else
			this.playerName = playerName;
		
		log = pcLog.getModule(this.playerName);
		
		checkAndRegisterClass(this);
	}
	
	private static void checkAndRegisterClass(PlayerClass playerClass) {
		if (registeredClasses.contains(playerClass.getClass()))
			return;
		
		//TODO: do registers.
	}

	public final String getPlayerName() {
		return playerName;
	}
	
	public final Player getPlayer() {
		return Bukkit.getPlayer(getPlayerName());
	}
	
	public final String getName() {
		return name;
	}
	
	public final String getUniqueID() {
		return uid;
	}
	
	public final void addExp(int amount) {
		getTier().addExp(amount);
	}
	
	public final int getMaxExp() {
		return getMaxExp(getCurrentTierLevel());
	}
	
	public final int getExp() {
		return getExp(getCurrentTierLevel());
	}
	
	public final IClassTier getTier() {
		return getTier(getCurrentTierLevel());
	}
	
	public final IClassTier getTier(int level) {
		if (hasTier(level))
			return tiers.get(level);
		return null;
	}
	
	public final boolean hasTier(int level) {
		return tiers.containsKey(level);
	}
	
	public final int getCurrentTierLevel() {
		return cTierLevel;
	}
	
	public void onLoad(ConfigurationNode node) {
		if (!node.get("uid", "").equals(getUniqueID()))
			throw new IllegalArgumentException("Configuration node was does not match UID. UID supposed to be " + getUniqueID() + " but got " + node.get("uid", "$BLANK$"));
		
		int a = node.get("current.tier", -1);
		if (a < 0) {
			if (a != -1) //Only throw error if tier did not exist. (-1 signifies it)
				log.severe(new StringBuilder().append("The configuration node for currently selected tier of the playerclass is invalid. It must be higher then 0. It is currently ").append(a).append("\n").append("Resetting value to 1").toString());
			a = 1;
		}
		
		setCurrentTier(a);
		
		checkTierCount(); //Check and repair tiers.
		
		for (IClassTier tier : tiers.values())
			tier.onLoad(node);
		
		load(node);
	}
	
	private void checkTierCount() {
		if (tiers.size() != getHighestPossibleTier())
		{
			pcLog.warning("Possible missing tiers for classes found.");
			log.severe("Tier count mismatch. Current tier count is " + tiers.size() + " when supposed to be " + getHighestPossibleTier());
			
			if (tiers.size() < getHighestPossibleTier())
			{
				int h = tiers.size() - 1;
				int i = h + 1;
				while (i < getHighestPossibleTier())
				{
					ClassTier dumb = new DummyClassTier(this, i);
					tiers.put(i, dumb);
					i++;
				}
			}
		
			//TODO: Self Fix code.
			
			//IF DUMMIES USED
//			log.severe("Dummy tiers were used for missing tier objects") //FIXME: Better debug message.
		}
	}

	public void onSave(ConfigurationNode node) {
		node = node.getNode(getUniqueID());
		node.set("current.tier", getCurrentTierLevel());
		
		checkTierCount(); //Check and repair tiers.
		
		for (IClassTier tier : tiers.values())
			tier.onSave(node);
		
		save(node);
	}
	
	/**
	 * {@inheritDoc} <br/><br/>
	 * <b>Warning. This is calculated every run!</b>
	 * @see com.noxpvp.mmo.classes.IPlayerClass#getTotalExp()
	 */
	public int getTotalExp() {
		int value = 0;
		for (Entry<Integer, IClassTier> tier : getTiers())
			if (canUseTier(tier.getKey()))
				value += tier.getValue().getExp();
		return value;
	}
	
	/**
	 * Save additional data to the node on player class saving. <br/><br/>
	 * <b>Warning the node is already in proper position. No need to re position node.</b>
	 * @param node The PlayerClass section of player configuration. 
	 */
	public abstract void save(ConfigurationNode node);
	
	/**
	 * Load additional data from the node on player class loading. <br/><br/>
	 * <b>Warning the node is already in proper position. No need to re position node.</b>
	 * <br/>
	 * <br/>
	 * You do not need to load the following. Just implement the set functions and it will work fine. <br/>
	 * <ul>
	 * 	<li></li>
	 * </ul>
	 * @param node The PlayerClass section of player configuration. 
	 */
	public abstract void load(ConfigurationNode node);
	
	protected abstract FileConfiguration getClassConfig();
	
////	Helper Methods
	public static int getTierLevel(IClassTier tier) {
		return tier.getTierLevel();
	}
	
	//THIS MUST BE STARTED ON ENABLE!
	@SuppressWarnings("rawtypes")
	public static void init() {
		pcLog = NoxMMO.getInstance().getModuleLogger(LOG_MODULE_NAME);
		registeredClasses = new ArrayList<Class>();
	}
}
