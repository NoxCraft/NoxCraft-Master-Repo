package com.noxpvp.mmo.classes;

import java.util.Map;

public abstract class BasePlayerClass implements PlayerClass {
	private final String name;
	private String displayName;
	
	private int levelCap;
	private int level;
	private int exp;
	
	public BasePlayerClass(String name, int levelCap)
	{
		this.name = name;
		this.levelCap = levelCap;
		level = 1;
		exp = 0;
	}
	
	public final String getDisplayName() {
		return (displayName == null)? getName() : displayName;
	}
	
	public final String getName() {
		return name;
	}
	
	public final PlayerClass setExp(int amount) {
		exp = amount;
		
		if (exp < 0)
		{
			level--;
			exp = getNextLevelExp() + exp;
			return this;
		} 
		else if (hasNextLevel())
		{
			exp = exp - getNextLevelExp();
			setLevel(level + 1);
		}
		
		return this;
	}
	
	public final PlayerClass setLevel(int amount) {
		level = amount;
		return this;
	}
	
	public final PlayerClass setLevelCap(int amount) {
		levelCap = amount;
		return this;
	}
	
	public PlayerClass setDisplayName(String name) {
		displayName = name;
		return this;
	}
	
	public final int getExp() {
		return exp;
	}
	
	public final int getLevel() {
		return level;
	}
	
	public final int getLevelCap() {
		return levelCap;
	}
	
	public int getLevelTotalExp() {
		int a = 0;
		for (int i = getLevel(); i > 0; i--)
			a += getLevelExp(i);
		
		return a;
	}
	
	public final boolean hasLevel(int level) {
		return getLevel() >= level;
	}
	
	public final boolean hasExp(int exp) {
		return getExp() >= exp;
	}
	
	public boolean hasNextLevel() {
		return getNextLevelExp() <= getExp();
	}
	
	public final int getLevelExp(int level)
	{
		return getLevelToExpMap().get(level);
	}
	
	public final int getNextLevelExp() {
		return getLevelExp(level + 1);
	}
	
	public final int getNeededExpForLevel() {
		return getLevelToExpMap().get(level + 1) - getExp();
	}
	
	public int getTotalExp() {
		return getExp() + getLevelTotalExp();
	}
	
	public abstract Map<Integer, Integer> getLevelToExpMap();
}
