package com.noxpvp.mmo.classes.tiers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.player.SkullSmasherAbility;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.PlayerClass;

public class AxesChampionClassTier extends ClassTier {

	public static final String TIER_NAME = "Champion";
	
	private volatile static String dName = "Champion";
	
	private Map<String, Ability> abilities = new HashMap<String, Ability>();
	private final double maxHealth;
	
	public AxesChampionClassTier(PlayerClass retainer) {
		super(retainer, TIER_NAME, 2);
		
		abilities.putAll(getRetainingClass().getTier(getTierLevel()-1).getAbilityMap());
		abilities.put("Skull Smasher", new SkullSmasherAbility(getPlayer()));
		
		this.maxHealth = 24;
	}

	public void setDisplayName(String displayName) {
		AxesChampionClassTier.dName = displayName;
	}

	public String getDisplayName() {
		return getRetainingClass().getColor() + AxesChampionClassTier.dName;
	}

	public List<String> getLore() {
		return Collections.emptyList();
	}
	
	public double getMaxHealth() {
		return this.maxHealth;
	}

	public int getMaxExp(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNeededExp() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNeededExp(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getExp(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLevel(int level) {
		// TODO Auto-generated method stub

	}

	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setExp(int amount) {
		// TODO Auto-generated method stub

	}

	public Map<String, Ability> getAbilityMap() {
		return Collections.unmodifiableMap(abilities);
	}

	public ExperienceType[] getExpTypes() {
		return ExperienceType.COMBAT;
	}

	@Override
	public void load(ConfigurationNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ConfigurationNode node) {
		// TODO Auto-generated method stub

	}

}
