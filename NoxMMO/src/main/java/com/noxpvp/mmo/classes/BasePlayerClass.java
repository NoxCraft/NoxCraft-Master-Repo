package com.noxpvp.mmo.classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BasePlayerClass implements PlayerClass {
	
	private static Map<Integer, BasePlayerClass> idToClassMap = new HashMap<Integer, BasePlayerClass>();
	
	public static Class<? extends BasePlayerClass> getPlayerClassJavaClass(int id) {
		BasePlayerClass c;
		return ((c = getPlayerClass(id)) == null)? null : c.getClass();
	}
	
	public static BasePlayerClass getPlayerClass(int id)
	{
		if (idToClassMap.containsKey(id))
			return idToClassMap.get(id);
		else
			return null;
	}
	
	public final int classId;

	private final String name;
	private String displayName;
	
	public double maxHealth;
	
	private Map<Integer, Integer> levelToExpMap;
	
	private int tierLevel;
	private int levelCap;
	private int level;
	private int exp;
	private final int baseExp;
	private final double exponent;
	private final float multiplier;
	private Set<String> abilities;
	
	public BasePlayerClass(int classID, String name, String displayName, double maxHealth, int tierLevel, int levelCap, double exponent, float multiplier)
	{
		this(classID, name, displayName, maxHealth, tierLevel, levelCap, exponent, multiplier, null);
	}
	
	public BasePlayerClass(int classId, String name, String displayName, double maxHealth, int tierLevel, int levelCap, double exponent, float multiplier, String[] abilities)
	{
		if (abilities == null)
			abilities = new String[0];
		
		this.abilities = new HashSet<String>(Arrays.asList(abilities));
		
		if (classId < 1) throw new IllegalArgumentException("classId must be more than 0");
		this.classId = classId;
		
		this.name = name;
		
		this.maxHealth = maxHealth;
		
		this.tierLevel = tierLevel;
		this.levelCap = levelCap;
		this.level = 1;
		this.exp = 0;
		this.baseExp = 2000;
		this.exponent = exponent;
		this.multiplier = multiplier;
		
		this.levelToExpMap = generateLevelToExpMap();
	}
	
	public final String getDisplayName() {
		return (displayName == null)? getName() : displayName;
	}
	
	public final String getName() {
		return name;
	}
	
	public final int getId() {
		return classId;
	}
	
	public double getMaxHealth() {
		return maxHealth;
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
			exp -= getNextLevelExp();
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
	
	public final int getBaseExp() {
		return baseExp;
	}
	
	public final int getTierLevel(){
		return this.tierLevel;
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
	
	public Map<Integer, Integer> getLevelToExpMap()
	{
		return this.levelToExpMap;
	}
	
	public final int getNextLevelExp() {
		return getLevelExp(level);
	}
	
	public final int getNeededExpForLevel() {
		return getLevelToExpMap().get(level) - getExp();
	}
	
	public int getTotalExp() {
		return getExp() + getLevelTotalExp();
	}
	
	protected Map<Integer, Integer> generateLevelToExpMap(){
		
		int l = level;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		while(l < levelCap){
			map.put(l, (int) (multiplier * (Math.pow(l++, exponent) + baseExp)));
		}
		
		return map;
	}
	
	public Set<String> getAbilityNames() {
		return abilities;
	}
}
