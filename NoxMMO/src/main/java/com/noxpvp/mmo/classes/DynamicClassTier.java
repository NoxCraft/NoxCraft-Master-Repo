package com.noxpvp.mmo.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.collections.InterpolatedMap;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.PlayerClass;

public class DynamicClassTier extends ClassTier {

	private String displayName;
	private Map<Integer, Integer> expMap = new HashMap<Integer, Integer>();
	private ExperienceType[] expTypes = new ExperienceType[0];
	
	private int level, maxLevel;
	private InterpolatedMap levelToExpMap = new InterpolatedMap();
	private List<String> lore = new ArrayList<String>();
	
	private String permNode = "";
	private boolean useLevelPerms = false;
	
	/**
	 * 
	 * @param name
	 * @param tierLevel current tier level
	 * @param maxLevel Maximum allowed level.
	 */
	public DynamicClassTier(PlayerClass retainer, String name, int tierLevel, @Nullable Integer maxLevel) {
		super(retainer, name, tierLevel);

		if (maxLevel == null || maxLevel < 0)
			this.maxLevel = Integer.MAX_VALUE;
	}

	@Override
	public boolean canUseLevel(int level) {
		if (super.canUseLevel(level)) {
			Player p = getPlayer();
			if (p != null) {
				if (useLevelPerms && !LogicUtil.nullOrEmpty(permNode))
					return NoxMMO.getInstance().getPermissionHandler().hasPermission(p, permNode);
				else
					return true;
			} else if (useLevelPerms)
				return false;
			return true;
		} else {
			return false;
		}
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getExp(int level) {
		return expMap.get(level);
	}
	
	public ExperienceType[] getExpTypes() {
		return expTypes;
	}
	
	public int getLevel() {
		return level;
	}

	public List<String> getLore() {
		return lore;
	}
	
	public int getMaxExp(int level) {
		return round(levelToExpMap.get(level));
	}

	public int getMaxLevel() {
		return maxLevel;
	}
	
	public int getNeededExp() {
		return getNeededExp(getLevel() + 1);
	}
	
	public int getNeededExp(int level) {
		
		return 0;
	}

	@Override
	public void load(ConfigurationNode node) { }

	/**
	 * Loads the tiers configuration. <br>
	 * 
	 * The node you supply must already be in the tiers section. 
	 * @param node the node to traverse for settings.
	 */
	public void loadTierConfig(ConfigurationNode node) {
		ConfigurationNode eNode = node.getNode("exp-levels");
		for (String lvl : eNode.getKeys())
			if (ParseUtil.isNumeric(lvl)) {
				int l = ParseUtil.parseInt(lvl, -1);
				if (l < 0)
					 continue;
				
				levelToExpMap.put(l, eNode.get(lvl, levelToExpMap.get(l)));
			}
		
		
				
	}

	@Override
	public void save(ConfigurationNode node) { }

	/**
	 * Saves the tiers configuration. <br>
	 * 
	 * The node you supply must already be in the tiers section. 
	 * @param node preselected node.
	 */
	public void saveTierConfig(ConfigurationNode node) {
		
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setExp(int amount) {
		boolean maxed = amount > getMaxExp();
		if (maxed)
			amount -= getMaxExp();
		
		if (amount < 0)
			amount = 0;
		expMap.put(getLevel(), getMaxExp());
		setLevel(getLevel() + 1);
		
		if (amount <= 0)
			return;
		
		setExp(amount);
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	private static int round(double value) {
		int ret = 0;
		
		ret = MathUtil.floor(value);
		
		return ret;
	}

	public Map<String, Ability> getAbilityMap() {
		return Collections.emptyMap();
	}
}
