package com.noxpvp.mmo.classes.player.main.axes;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.abilities.entity.LeapAbility;
import com.noxpvp.mmo.abilities.player.SkullSmasherAbility;
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

	public final static int classId = 2;
	public final static String className = "champion";
	public final static String DisplayName = ChatColor.RED + "Champion";
	
	public final static String[] STANDARD_ABILITIES = new String[] { LeapAbility.ABILITY_NAME, SkullSmasherAbility.ABILITY_NAME };
	
	public final static double maxHealth = 24;
	public final static int tierLevel = 3;
	public final static int levelCap = 100;
	public final static double exponent = 2.4;
	public final static float multiplier = 1.0f;
	
	public ChampionClass() {
		super(ChampionClass.classId, ChampionClass.className, ChampionClass.DisplayName, ChampionClass.maxHealth,
				ChampionClass.tierLevel, ChampionClass.levelCap, ChampionClass.exponent, ChampionClass.multiplier, STANDARD_ABILITIES);
	}

}
