package com.noxpvp.mmo.classes.player.main.axes;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.abilities.entity.LeapAbility;
import com.noxpvp.mmo.abilities.player.HammerOfThorAbility;
import com.noxpvp.mmo.abilities.player.MassDestructionAbility;
import com.noxpvp.mmo.abilities.player.SkullSmasherAbility;
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
	public final static String[] STANDARD_ABILITIES = new String[] { LeapAbility.ABILITY_NAME, SkullSmasherAbility.ABILITY_NAME };
	private final static String DisplayName = ChatColor.RED + "Berserker";
	
	public final static double maxHealth = 26;
	public final static int tierLevel = 2;
	public final static int levelCap = 100;
	public final static double exponent = 2.6;
	public final static float multiplier = 1.0f;
	
	public BerserkerClass() {
		super(BerserkerClass.classId, BerserkerClass.className, BerserkerClass.DisplayName, BerserkerClass.maxHealth,
				BerserkerClass.tierLevel, BerserkerClass.levelCap, BerserkerClass.exponent, BerserkerClass.multiplier);
	}

}
