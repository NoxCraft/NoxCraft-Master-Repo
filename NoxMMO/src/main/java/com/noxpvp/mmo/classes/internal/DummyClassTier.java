package com.noxpvp.mmo.classes.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.mmo.abilities.Ability;

public class DummyClassTier extends ClassTier {

	private final ExperienceType[] b = new ExperienceType[0];
	private final List<String> b2 = new ArrayList<String>();

	public DummyClassTier(PlayerClass retainer, int tierLevel) {
		super(retainer, "DummyTier", tierLevel);
	}
	
	public void addExp(int amount) { }

	public String getDisplayName() {
		return "";
	}

	public int getLevel() {
		return 0;
	}

	public int getMaxExp(int level) {
		return 0;
	}

	public int getMaxLevel() {
		return 0;
	}

	public int getNeededExp() {
		return 0;
	}

	public int getNeededExp(int level) {
		return 0;
	}

	@Override
	public void load(ConfigurationNode node) { }

	public void removeExp(int amount) { }

	@Override
	public void save(ConfigurationNode node) { }

	public void setDisplayName(String displayName) { }

	public void setExp(int amount) { }

	public int getExp(int level) {
		return 0;
	}

	public void setLevel(int level) { }

	public List<String> getLore() {
		return b2;
	}

	public ExperienceType[] getExpTypes() {
		return b ;
	}

	public Map<String, Ability> getAbilityMap() {
		return Collections.emptyMap();
	}

	public List<Ability> getAbilities() {
		return Collections.emptyList();
	}

}
