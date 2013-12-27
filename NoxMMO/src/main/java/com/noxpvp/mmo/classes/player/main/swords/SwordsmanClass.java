package com.noxpvp.mmo.classes.player.main.swords;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Swordsman<br/>
 * ID:			11<br/>
 * Health:		22<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.2<br/>
 * Multiplier:	1.0<br/>
 */
public class SwordsmanClass extends BasePlayerClass{

	public final static int classId = 11;
	public final static String className = "swordsman";
	public final static String DisplayName = ChatColor.DARK_BLUE + "Swordsman";
	
	public final static double maxHealth = 22;
	public final static int tierLevel = 1;
	public final static int levelCap = 100;
	public final static double exponent = 2.2;
	public final static float multiplier = 1.0f;
	
	public SwordsmanClass() {
		super(SwordsmanClass.classId, SwordsmanClass.className, SwordsmanClass.DisplayName, SwordsmanClass.maxHealth,
				SwordsmanClass.tierLevel, SwordsmanClass.levelCap, SwordsmanClass.exponent, SwordsmanClass.multiplier);
	}

}
