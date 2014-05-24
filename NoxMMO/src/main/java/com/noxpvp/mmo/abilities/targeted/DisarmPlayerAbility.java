package com.noxpvp.mmo.abilities.targeted;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;

/**
 * @author NoxPVP
 */
public class DisarmPlayerAbility extends BaseTargetedPlayerAbility implements IPVPAbility {

	private static final String ABILITY_NAME = "Disarm";
	public static final String PERM_NODE = "disarm";
	
	private List<LivingEntity> disarmed = new ArrayList<LivingEntity>();

	public DisarmPlayerAbility(Player player) {
		this(player, 10);
	}

	public DisarmPlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		disarmed.add(getTarget());

		return new AbilityResult(this, true);
	}

}
