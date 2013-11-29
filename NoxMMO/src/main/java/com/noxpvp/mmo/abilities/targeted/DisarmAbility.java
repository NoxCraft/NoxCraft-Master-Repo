package com.noxpvp.mmo.abilities.targeted;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class DisarmAbility extends BaseTargetedPlayerAbility{
	
	public static List<LivingEntity> disarmed = new ArrayList<LivingEntity>();
	
	private final static String ABILITY_NAME = "Disarm";
	public static final String PERM_NODE = "disarm";
	
	/**
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public DisarmAbility(Player player){
		super(ABILITY_NAME, player, NoxMMO.getInstance().getPlayerManager().getMMOPlayer(player).getTarget());
	}
	
	/**
	 * 
	 * 
	 * @return Boolean - If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		DisarmAbility.disarmed.add(getTarget());
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean - If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		if (getPlayer() == null || getTarget() == null)
			return false;
		
		return true;
	}
	
}
