package com.noxpvp.mmo.classes.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.locale.MMOLocale;

public class DummyClass implements IPlayerClass {

	public static final DummyClass PRIMARY = new DummyClass(true);
	public static final DummyClass SECONDARY = new DummyClass(false);

	private static final ClassType[] EMPTY_CLASSTYPE = new ClassType[0];

	private static final ExperienceType[] EMPTY_EXPTYPE = new ExperienceType[0];
	private final Map<Integer, IClassTier> tiers = new HashMap<Integer, IClassTier>();

	private final boolean isPrimary;

	private DummyClass(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public boolean isPrimaryClass() {
		return isPrimary;
	}

	public ClassType getPrimaryClassType() {
		return null;
	}

	public ClassType[] getSubClassTypes() {
		return EMPTY_CLASSTYPE;
	}

	public ItemStack getIdentifingItem() {
		ItemStack ret = new ItemStack(Material.SIGN);
		ret.getItemMeta().setDisplayName(getDisplayName());
		ret.getItemMeta().setLore(getLore());

		return ret;
	}

	public Color getRBGColor() {
		return null;
	}

	public Color getBaseArmourColor() {
		return null;
	}

	public ChatColor getColor() {
		return null;
	}

	public ExperienceType[] getExpTypes() {
		return EMPTY_EXPTYPE;
	}

	public boolean canUseTier(int tier) {
		return true;
	}

	public int getHighestPossibleTier() {
		return 1;
	}

	public String getUniqueID() {
		return "-1";
	}

	public String getName() {
		return "Dummy class";
	}
	
	public String getDescription() {
		return "Dummy class";
	}
	
	public String getDescription(ChatColor color) {
		return color + "Dummy class";
	}
	
	public List<String> getLore(int lineLength) {
		return new ArrayList<String>();
	}
	
	public List<String> getLore(ChatColor color, int lineLength) {
		return new ArrayList<String>();
	}

	public List<String> getLore() {
		return new ArrayList<String>();
	}

	public Set<Entry<Integer, IClassTier>> getTiers() {
		return tiers.entrySet();
	}

	public boolean canUseClass() {
		return true;
	}

	public IClassTier getTier() {
		return tiers.get(1);
	}

	public void setCurrentTier(IClassTier tier) {
	}

	public void setCurrentTier(int tierLevel) {
	}

	public boolean hasTier(int level) {
		return level == 1;
	}

	public int getCurrentTierLevel() {
		return 1;
	}

	public IClassTier getTier(int tier) {
		if (tier == 1)
			return getTier();
		return null;
	}

	public int getLatestTier() {
		return 1;
	}

	public int getHighestAllowedTier() {
		return 1;
	}

	public String getDisplayName() {
		return MMOLocale.CLASS_DISPLAY_NAME.get("None", "&7");
	}

	public int getExp() {
		return 0;
	}

	public void setExp(int amount) {
	}

	public int getExp(int tier) {
		return 0;
	}

	public int getTotalExp() {
		return 0;
	}

	public int getMaxExp() {
		return 0;
	}

	public int getMaxExp(int tier) {
		return 0;
	}

	public int getLevel() {
		return 1;
	}

	public int getLevel(int tier) {
		return 1;
	}

	public int getMaxLevel() {
		return 1;
	}

	public int getMaxLevel(int tier) {
		return 1;
	}

	public int getTotalLevel() {
		return 0;
	}

	public void setExp(int tier, int amount) {
	}

	public void addExp(int amount) {
	}

	public void addExp(int tier, int amount) {
	}

	public void removeExp(int amount) {
	}

	public void removeExp(int tier, int amount) {
	}

	public void onLoad(ConfigurationNode node) {
	}

	public void onSave(ConfigurationNode node) {
	}

	public Map<String, ? extends Ability> getAbilityMap() {
		return new HashMap<String, Ability>();
	}

	public Collection<? extends Ability> getAbilities() {
		return new ArrayList<Ability>();
	}
}
