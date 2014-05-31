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

package com.noxpvp.mmo.classes;

import java.util.ArrayList;
import java.util.List;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;

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

}
