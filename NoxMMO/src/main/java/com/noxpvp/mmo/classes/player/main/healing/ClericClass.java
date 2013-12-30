package com.noxpvp.mmo.classes.player.main.healing;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.abilities.player.MedPackAbility;
import com.noxpvp.mmo.abilities.targeted.SootheAbility;
import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Cleric<br/>
 * ID:			71<br/>
 * Health:		22<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.2<br/>
 * Multiplier:	1.0<br/>
 */
public class ClericClass extends BasePlayerClass{

	public final static int classId = 71;
	public final static String className = "cleric";
	public final static String DisplayName = ChatColor.GREEN + "Cleric";
	
	public final static double maxHealth = 22;
	public final static int tierLevel = 1;
	public final static int levelCap = 100;
	public final static double exponent = 2.2;
	public final static float multiplier = 1.0f;
	
	public final static String[] STANDARD_ABILITIES = new String[] { 
		SootheAbility.ABILITY_NAME,
		MedPackAbility.ABILITY_NAME
	};
	
	public ClericClass() {
		super(ClericClass.classId, ClericClass.className, ClericClass.DisplayName, ClericClass.maxHealth,
				ClericClass.tierLevel, ClericClass.levelCap, ClericClass.exponent, ClericClass.multiplier);
	}

}
