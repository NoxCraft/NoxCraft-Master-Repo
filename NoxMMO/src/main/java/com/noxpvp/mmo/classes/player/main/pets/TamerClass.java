package com.noxpvp.mmo.classes.player.main.pets;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Tamer<br/>
 * ID:			51<br/>
 * Health:		22<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.2<br/>
 * Multiplier:	1.0<br/>
 */
public class TamerClass extends BasePlayerClass{

	public final static int classId = 51;
	public final static String className = "tamer";
	public final static String DisplayName = ChatColor.DARK_AQUA + "Tamer";
	
	public final static double maxHealth = 22;
	public final static int tierLevel = 1;
	public final static int levelCap = 100;
	public final static double exponent = 2.2;
	public final static float multiplier = 1.0f;
	
	public TamerClass() {
		super(TamerClass.classId, TamerClass.className, TamerClass.DisplayName, TamerClass.maxHealth,
				TamerClass.tierLevel, TamerClass.levelCap, TamerClass.exponent, TamerClass.multiplier);
	}

}
