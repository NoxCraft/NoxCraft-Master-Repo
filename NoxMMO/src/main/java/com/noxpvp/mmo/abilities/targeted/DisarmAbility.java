package com.noxpvp.mmo.abilities.targeted;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;

/**
 * @author NoxPVP
 *
 */
public class DisarmAbility extends BaseTargetedPlayerAbility implements PVPAbility {
	
	public static List<LivingEntity> disarmed = new ArrayList<LivingEntity>();
	
	private final static String ABILITY_NAME = "Disarm";
	public static final String PERM_NODE = "disarm";
	
	/**
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public DisarmAbility(Player player){
		super(ABILITY_NAME, player, PlayerManager.getInstance().getPlayer(player).getTarget());
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		DisarmAbility.disarmed.add(getTarget());
		
		return true;
	}
	
}
