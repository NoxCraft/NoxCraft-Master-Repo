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

import java.util.List;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;

public interface IClassTier {
	
	public int getTierLevel();
	
	public String getName();
	
	public void setDisplayName(String displayName);
	public String getDisplayName();
	public List<String> getLore();
	
	public int getMaxExp();
	public int getMaxExp(int level);
	public int getNeededExp();
	public int getNeededExp(int level);
	
	public int getExp();
	public int getExp(int level);
	public int getTotalExp();
	public void setTotalExp(int amount);
	
	public void setLevel(int level);
	public int getLevel();
	public boolean canUseLevel(int level);
	
	public int getMaxLevel();
	
	public void setExp(int amount);
	public void addExp(int amount);
	public void removeExp(int amount);
	
	public ExperienceType[] getExpTypes();
	
	public void onSave(ConfigurationNode node);
	public void onLoad(ConfigurationNode node);
	
	/**
	 * Used to cleanup data if max exp per level changes and such. <br/>
	 * This is a very intensive operation and must be used sparingly. <br/>
	 * <br/>
	 * This method should include usage of the setTotalExp(int value);
	 * <br/>
	 * This will allow auto fixing values when manually setting total exp. Thus cleanup can include that set.
	 */
	public void update();
}
