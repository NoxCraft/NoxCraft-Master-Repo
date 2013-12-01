package com.noxpvp.mmo.classes.player;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Berserker<br/>
 * ID:			3<br/>
 * Health:		26<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.6<br/>
 * Multiplier:	1.0<br/>
 */
public class BerserkerClass extends BasePlayerClass{

	public final static int classId = 3;
	public final static String className = "berserker";
	private final static String DisplayName = ChatColor.RED + "Berserker";
	
	private final static double maxHealth = 26;
	private final static int tierLevel = 2;
	private final static int levelCap = 100;
	private final static double exponent = 2.6;
	private final static float multiplier = 1.0f;
	
	public BerserkerClass() {
		super(BerserkerClass.classId, BerserkerClass.className, BerserkerClass.DisplayName, BerserkerClass.maxHealth,
				BerserkerClass.tierLevel, BerserkerClass.levelCap, BerserkerClass.exponent, BerserkerClass.multiplier);
	}

}
