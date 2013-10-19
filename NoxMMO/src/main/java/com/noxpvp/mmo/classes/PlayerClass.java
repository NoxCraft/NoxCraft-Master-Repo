package com.noxpvp.mmo.classes;

public interface PlayerClass {
	public String getName();
	
	public int getTotalExp();
	public int getExp();
	public int getLevel();
	
	public int getNextLevelExp();
	public int getLevelExp(int level);
	public int getLevelTotalExp();
	
	public int getNeededExpForLevel();
	
	
	public int getLevelCap();

	public PlayerClass setExp(int amount);
	public PlayerClass setLevel(int amount);
	public PlayerClass setLevelCap(int amount);

	public PlayerClass setDisplayName(String name);
	public String getDisplayName();
	
	public boolean hasNextLevel();
	
	public boolean hasLevel(int level);
	public boolean hasExp(int exp);
	
}
