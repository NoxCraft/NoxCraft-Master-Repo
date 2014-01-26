package com.noxpvp.mmo.classes;

import java.util.Set;

import com.noxpvp.mmo.listeners.ExperienceListener.experienceType;

public interface PlayerClass {
	public int getId();
	public String getName();
	
	public double getMaxHealth();
	
	public int getTierLevel();
	public int getTotalLevels();
	public int getTotalExp();
	public int getExp();
	public int getLevel();
	
	public int getNextLevelExp();
	public int getLevelExp(int level);
	public int getLevelTotalExp();
	
	public int getNeededExpForLevel();
	public boolean canGainExp(experienceType entityKill);
	
	public int getLevelCap();

	public PlayerClass addExp(int amount);
	public PlayerClass setExp(int amount);
	public PlayerClass setLevel(int amount);
	public PlayerClass setLevelCap(int amount);

	public PlayerClass setDisplayName(String name);
	public String getDisplayName();
	
	public boolean hasNextLevel();
	
	public boolean hasLevel(int level);
	public boolean hasExp(int exp);
	
	public Set<String> getAbilityNames();
}
