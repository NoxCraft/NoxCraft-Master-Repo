package com.noxpvp.mmo.classes.player;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Champion<br/>
 * ID:			2<br/>
 * Health:		24<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.4<br/>
 * Multiplier:	1.0<br/>
 */
public class ChampionClass extends BasePlayerClass{

	private final static int classId = 2;
	private final static String className = "champion";
	private final static String DisplayName = ChatColor.RED + "Champion";
	
	private final static double maxHealth = 24;
	private final static int tierLevel = 3;
	private final static int levelCap = 100;
	private final static double exponent = 2.4;
	private final static float multiplier = 1.0f;
	
	public ChampionClass() {
		super(ChampionClass.classId, ChampionClass.className, ChampionClass.DisplayName, ChampionClass.maxHealth,
				ChampionClass.tierLevel, ChampionClass.levelCap, ChampionClass.exponent, ChampionClass.multiplier);
	}

}