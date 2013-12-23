package com.noxpvp.mmo.classes.player.main.stealth;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Thief<br/>
 * ID:			41<br/>
 * Health:		22<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.2<br/>
 * Multiplier:	1.0<br/>
 */
public class ThiefClass extends BasePlayerClass{

	public final static int classId = 41;
	public final static String className = "thief";
	public final static String DisplayName = ChatColor.DARK_GRAY + "Thief";
	
	public final static double maxHealth = 22;
	public final static int tierLevel = 1;
	public final static int levelCap = 100;
	public final static double exponent = 2.2;
	public final static float multiplier = 1.0f;
	
	public ThiefClass() {
		super(ThiefClass.classId, ThiefClass.className, ThiefClass.DisplayName, ThiefClass.maxHealth,
				ThiefClass.tierLevel, ThiefClass.levelCap, ThiefClass.exponent, ThiefClass.multiplier);
	}

}
