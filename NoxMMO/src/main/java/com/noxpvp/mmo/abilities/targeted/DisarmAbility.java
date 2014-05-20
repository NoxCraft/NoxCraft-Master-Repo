package com.noxpvp.mmo.abilities.targeted;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.MMOPlayerManager;
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

	
	public DisarmAbility(Player player) {
		this(player, 10);
	}
	
	public DisarmAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		DisarmAbility.disarmed.add(getTarget());
		
		return true;
	}
	
}
