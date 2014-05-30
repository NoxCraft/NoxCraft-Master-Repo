package com.noxpvp.mmo.classes.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.meta.When;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.annotation.Temporary;
import com.noxpvp.core.gui.MenuItemRepresentable;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.classes.DynamicClassTier;
import com.noxpvp.mmo.util.PlayerClassUtil;

/**
 * When Implementing you must use all of the following constructors and params.
 * <br/>
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
 */
public abstract class PlayerClass implements IPlayerClass, MenuItemRepresentable {

	public static final String LOG_MODULE_NAME = "PlayerClass";
	private static final String DYNAMIC_TIER_PATH = "dynamic.tiers";
	//Debug and errors
	protected static ModuleLogger pcLog;

	@SuppressWarnings("rawtypes")
	private static List<Class> registeredClasses;
	//Identification
	private final String uid;
	protected ModuleLogger log;
	//Tiers
	protected Map<Integer, IClassTier> tiers;
	private int cTierLevel = 1;
	private String name;
	private ItemStack identiferItem;
	//Player Data
	private String playerName;

	public PlayerClass(String uid, @Nonnull String name, @Nonnull Player player) {
		this(uid, name, null, player);
	}

	public PlayerClass(String uid, @Nonnull String name, @Nonnull String playerName) {
		this(uid, name, playerName, null);
	}

	public PlayerClass(String uid,
	                   @Nonnull String name,
	                   @Nonnull(when = When.MAYBE) String playerName, @Nonnull(when = When.MAYBE) Player player) {
		Validate.notNull(name, "The name of class must not be null!");
		Validate.notNull(uid, "The UID of class must not be null!");
		Validate.isTrue((player != null || playerName != null), "Either the player or the playerName must not be null!");

		this.uid = uid;
		this.name = name;

		if (playerName == null)
			this.playerName = player.getName();
		else
			this.playerName = playerName;

		log = pcLog.getModule(this.playerName);

		this.tiers = craftClassTiers();
		this.tiers.putAll(craftDynamicTiers());

		setCurrentTier(getCurrentTierLevel());

		NoxMMO mmo = NoxMMO.getInstance();
		for (IClassTier tier : tiers.values()) {
			mmo.addPermission(new NoxPermission(NoxMMO.getInstance(),
					StringUtil.join(".", NoxMMO.PERM_NODE, "class", tier.getRetainingClass().getName(), "tier", Integer.toString(tier.getTierLevel())),
					"Allows the usage of the " + tier.getName() + " tier in the " + tier.getRetainingClass().getName() + " class",
					PermissionDefault.OP));
		}

		checkAndRegisterClass(this);
	}

	private static void checkAndRegisterClass(PlayerClass playerClass) {
		if (registeredClasses.contains(playerClass.getClass()))
			return;

		PlayerClassUtil.registerPlayerClass(playerClass.getClass());
	}

	//// START TEMP

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

	//// END TEMP

	public final void addExp(int amount) {
		getTier().addExp(amount);
	}

	public void addExp(int tier, int amount) {
		getTier(tier).addExp(amount);
	}

	public boolean canUseClass() {
		if (getPlayer() != null)
			return getPlayer().hasPermission(getPermission());
		return false;
	}

	public String getPermission() {
		return StringUtil.join(".", "nox", "class", getName());
	}

	private void checkTierCount() {
		if (tiers.size() != getHighestPossibleTier()) {
			pcLog.warning("Possible missing tiers for classes found.");
			log.severe("Tier count mismatch. Current tier count is " + tiers.size() + " when supposed to be " + getHighestPossibleTier());

			if (tiers.size() < getHighestPossibleTier()) {
				int h = tiers.size() - 1;
				int i = h + 1;
				while (i < getHighestPossibleTier()) {
					ClassTier dumb = new DummyClassTier(this, i);
					tiers.put(i, dumb);
					i++;
				}
			}


			//TODO: Message about missing tiers... URRRRGGGHHHh

			//IF DUMMIES USED
//			log.severe("Dummy tiers were used for missing tier objects") //FIXME: Better debug message.
		}
	}

	protected abstract Map<Integer, IClassTier> craftClassTiers();

	protected final Map<Integer, IClassTier> craftDynamicTiers() {
		return craftDynamicTiers(getClassConfig());
	}

	private Map<Integer, IClassTier> craftDynamicTiers(ConfigurationNode node) {
		Map<Integer, IClassTier> nTiers = new HashMap<Integer, IClassTier>();

		if (node != null) {
			node = node.getNode(DYNAMIC_TIER_PATH);
			for (ConfigurationNode t : node.getNodes()) {
				int tl = ParseUtil.parseInt(t.getName(), -1);
				if (tl < 0) {
					pcLog.warning("Tier level must be greater than 0. One of the dynamic tiers is invalid.");
					continue;
				}

				if (!t.contains("name")) {
					pcLog.severe("One of the nodes for dynamic tiers in a player class is missing a name value.");
					continue;
				}

				int maxLvl = t.get("max-level", -1);
				DynamicClassTier ti = new DynamicClassTier(this, t.get("name", String.class), tl, maxLvl);
				ti.loadTierConfig(t);
				nTiers.put(tl, ti);
			}

		}

		return nTiers;
	}

	@Temporary
	public Collection<Ability> getAbilities() {
		return getAbilityMap().values();
	}

	@Temporary
	public Map<String, Ability> getAbilityMap() {
		return getTier().getAbilityMap();
	}

	protected abstract FileConfiguration getClassConfig();

	public final int getCurrentTierLevel() {
		return cTierLevel;
	}

	public final int getExp() {
		return getExp(getCurrentTierLevel());
	}

	public final void setExp(int amount) {
		setExp(getCurrentTierLevel(), amount);
	}

	public int getExp(int tier) {
		return getTier(tier).getExp();
	}

	public int getHighestAllowedTier() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLatestTier() {
		// TODO Auto-generated method stub
		return 0;
	}

	public final int getLevel() {
		return getLevel(getCurrentTierLevel());
	}

	public int getLevel(int tier) {
		return getTier(tier).getLevel();
	}

	public final int getMaxExp() {
		return getMaxExp(getCurrentTierLevel());
	}

	public int getMaxExp(int tier) {
		return getTier(tier).getMaxExp();
	}

	public final int getMaxLevel() {
		return getMaxLevel(getCurrentTierLevel());
	}

	public int getMaxLevel(int tier) {
		return getTier().getMaxLevel();
	}

	public final String getName() {
		return name;
	}

	public String getDisplayName() {
		return getColor() + getName();
	}
	
	public String getDescription() {
		return "\"Something descriptive here\"";
	}
	
	public String getDescription(ChatColor color) {
		return color + getDescription();
	}
	
	public List<String> getLore() {
		return getLore(30);
	}
	
	public List<String> getLore(int lineLength) {
		return MessageUtil.convertStringForLore(getDescription(), lineLength);
	}
	
	public List<String> getLore(ChatColor color, int lineLength) {
		List<String> ret = new ArrayList<String>();
		for (String lore : getLore(lineLength))
			ret.add(color + lore);
		
		return ret;
	}
	
	public ItemStack getIdentifiableItem() {
		if (identiferItem == null) {
			identiferItem = new ItemStack(Material.BOOK_AND_QUILL);

			ItemMeta meta = identiferItem.getItemMeta();
			meta.setDisplayName(new MessageBuilder().gold(ChatColor.BOLD + "Class: ")
					.append(getColor() + getName()).toString());
			
			List<String> lore = getLore(ChatColor.GOLD, 28);
			
			lore.add(new MessageBuilder().gold("Current Tier: ").yellow(getCurrentTierLevel()).toString());
			
			meta.setLore(lore);

			identiferItem.setItemMeta(meta);
		}

		return identiferItem.clone();
	}

	public final Player getPlayer() {
		return Bukkit.getPlayer(getPlayerName());
	}

	public final String getPlayerName() {
		return playerName;
	}

	public final IClassTier getTier() {
		return getTier(getCurrentTierLevel());
	}

	public final IClassTier getTier(int level) {
		if (hasTier(level))
			return tiers.get(level);
		return null;
	}

	public final Set<Entry<Integer, IClassTier>> getTiers() {
		return tiers.entrySet();
	}

	/**
	 * {@inheritDoc} <br/><br/>
	 * <b>Warning. This is calculated every run!</b>
	 *
	 * @see com.noxpvp.mmo.classes.internal.IPlayerClass#getTotalExp()
	 */
	public int getTotalExp() {
		int value = 0;
		for (Entry<Integer, IClassTier> tier : getTiers())
			if (canUseTier(tier.getKey()))
				value += tier.getValue().getExp();
		return value;
	}

	public int getTotalLevel() {
		int ret = 0;
		for (IClassTier tier : tiers.values())
			ret += tier.getLevel();
		return ret;
	}

	public final String getUniqueID() {
		return uid;
	}

	public final boolean hasTier(int level) {
		return tiers.containsKey(level);
	}

	/**
	 * Load additional data from the node on player class loading. <br/><br/>
	 * <b>Warning the node is already in proper position. No need to re position node.</b>
	 * <br/>
	 * <br/>
	 * You do not need to load the following. Just implement the set functions and it will work fine. <br/>
	 * <ul>
	 * <li></li>
	 * </ul>
	 *
	 * @param node The PlayerClass section of player configuration.
	 */
	public abstract void load(ConfigurationNode node);

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

	public void onSave(ConfigurationNode node) {
		node = node.getNode(getUniqueID().toString());
		node.set("current.tier", getCurrentTierLevel());

		checkTierCount(); //Check and repair tiers.

		for (IClassTier tier : tiers.values())
			tier.onSave(node);

		save(node);
	}

	public final void removeExp(int amount) {
		removeExp(getCurrentTierLevel(), amount);
	}

	public void removeExp(int tier, int amount) {
		getTier(tier).removeExp(amount);
	}

	/**
	 * Save additional data to the node on player class saving. <br/><br/>
	 * <b>Warning the node is already in proper position. No need to re position node.</b>
	 *
	 * @param node The PlayerClass section of player configuration.
	 */
	public abstract void save(ConfigurationNode node);

	public void setCurrentTier(IClassTier tier) {
		setCurrentTier(tier.getLevel());
	}

	public void setCurrentTier(int tierLevel) {
		cTierLevel = tierLevel;

		MMOPlayer p = MMOPlayerManager.getInstance().getPlayer(getPlayer());
		if (p.getPrimaryClass() == this)
			p.getPlayer().setMaxHealth(getTier().getMaxHealth());

	}

	public void setExp(int tier, int amount) {
		getTier(tier).setExp(amount);
	}
}
