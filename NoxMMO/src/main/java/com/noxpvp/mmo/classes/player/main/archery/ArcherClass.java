package com.noxpvp.mmo.classes.player.main.archery;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Archer<br/>
 * ID:			31<br/>
 * Health:		22<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.2<br/>
 * Multiplier:	1.0<br/>
 */
public class ArcherClass extends BasePlayerClass{

	public final static int classId = 31;
	public final static String className = "archer";
	public final static String DisplayName = ChatColor.RED + "Archer";
	
	public final static double maxHealth = 22;
	public final static int tierLevel = 1;
	public final static int levelCap = 100;
	public final static double exponent = 2.2;
	public final static float multiplier = 1.0f;
	
	public ArcherClass() {
		super(ArcherClass.classId, ArcherClass.className, ArcherClass.DisplayName, ArcherClass.maxHealth,
				ArcherClass.tierLevel, ArcherClass.levelCap, ArcherClass.exponent, ArcherClass.multiplier);
	}

}
