package com.noxpvp.mmo.classes.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.IronGolem;

import com.noxpvp.mmo.abilities.entity.LeapAbility;
import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Basher<br/>
 * ID:			1<br/>
 * Health:		22<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.2<br/>
 * Multiplier:	1.0<br/>
 */
public class BasherClass extends BasePlayerClass{

	public final static int classId = 1;
	public final static String className = "basher";
	public final static String[] STANDARD_ABILITIES = new String[] { LeapAbility.ABILITY_NAME };
	private final static String DisplayName = ChatColor.RED + "Basher";
	
	private final static double maxHealth = 22;
	private final static int tierLevel = 1;
	private final static int levelCap = 100;
	private final static double exponent = 2.2;
	private final static float multiplier = 1.0f;
	
	public BasherClass() {
		super(BasherClass.classId, BasherClass.className, BasherClass.DisplayName, BasherClass.maxHealth,
				BasherClass.tierLevel, BasherClass.levelCap, BasherClass.exponent, BasherClass.multiplier, STANDARD_ABILITIES);
	}

}
