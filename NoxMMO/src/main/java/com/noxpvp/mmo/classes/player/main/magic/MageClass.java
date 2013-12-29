package com.noxpvp.mmo.classes.player.main.magic;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.abilities.player.FireBallAbility;
import com.noxpvp.mmo.abilities.player.RejuvenationAbility;
import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Mage<br/>
 * ID:			61<br/>
 * Health:		22<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.2<br/>
 * Multiplier:	1.0<br/>
 */
public class MageClass extends BasePlayerClass{

	public final static int classId = 61;
	public final static String className = "mage";
	public final static String DisplayName = ChatColor.LIGHT_PURPLE + "Mage";
	
	public final static double maxHealth = 22;
	public final static int tierLevel = 1;
	public final static int levelCap = 100;
	public final static double exponent = 2.2;
	public final static float multiplier = 1.0f;
	
	public final static String[] STANDARD_ABILITIES = new String[] { 
		RejuvenationAbility.ABILITY_NAME,
		FireBallAbility.ABILITY_NAME
	};
	
	public MageClass() {
		super(MageClass.classId, MageClass.className, MageClass.DisplayName, MageClass.maxHealth,
				MageClass.tierLevel, MageClass.levelCap, MageClass.exponent, MageClass.multiplier);
	}

}
