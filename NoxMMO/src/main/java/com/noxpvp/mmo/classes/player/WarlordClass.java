package com.noxpvp.mmo.classes.player;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.abilities.entity.LeapAbility;
import com.noxpvp.mmo.abilities.player.HammerOfThorAbility;
import com.noxpvp.mmo.abilities.player.MassDestructionAbility;
import com.noxpvp.mmo.abilities.player.SkullSmasherAbility;
import com.noxpvp.mmo.classes.BasePlayerClass;

/**
 * Class Name:	Warlord<br/>
 * ID:			4<br/>
 * Health:		28<br/>
 * Level Cap:	100<br/>
 * Exponent:	2.8<br/>
 * Multiplier:	1.0<br/>
 */
public class WarlordClass extends BasePlayerClass{

	public final static int classId = 4;
	public final static String className = "warlord";
	public final static String[] STANDARD_ABILITIES = new String[] {  LeapAbility.ABILITY_NAME, SkullSmasherAbility.ABILITY_NAME, MassDestructionAbility.ABILITY_NAME, HammerOfThorAbility.ABILITY_NAME };  
	private final static String DisplayName = ChatColor.RED + "Warlord";
	
	private final static double maxHealth = 28;
	private final static int tierLevel = 4;
	private final static int levelCap = 100;
	private final static double exponent = 2.8;
	private final static float multiplier = 1.0f;
	
	public WarlordClass() {
		super(WarlordClass.classId, WarlordClass.className, WarlordClass.DisplayName, WarlordClass.maxHealth,
				WarlordClass.tierLevel, WarlordClass.levelCap, WarlordClass.exponent, WarlordClass.multiplier);
	}

}