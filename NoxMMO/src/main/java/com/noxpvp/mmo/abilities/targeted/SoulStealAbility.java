package com.noxpvp.mmo.abilities.targeted;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class SoulStealAbility extends BaseTargetedPlayerAbility{
	
	public static final String PERM_NODE = "soulsteal";
	private static final String ABILITY_NAME = "SoulSteal";
	private int duration;
	
	/**
	 * 
	 * 
	 * @return Integer The currently set duration for the blindness effect
	 */
	public int getDuration() {return duration;}
	
	/**
	 * 
	 * 
	 * @param duration The duration is ticks for the blindness effect to last
	 * @return SoulStealAbility This instance, used for chaining
	 */
	public SoulStealAbility setDuration(int duration) {this.duration = duration; return this;}
	
	/**
	 * 
	 * 
	 * @param player The Player type user for this ability instance
	 */
	public SoulStealAbility(Player player){
		super(ABILITY_NAME, player, NoxMMO.getInstance().getPlayerManager().getMMOPlayer(player).getTarget());
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		getTarget().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));
		
		return true;
	}
	
}
