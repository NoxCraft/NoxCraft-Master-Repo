package com.noxpvp.mmo.abilities.targeted;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;

public class BoltAbility extends BaseTargetedPlayerAbility implements PVPAbility {
	
	private final static String ABILITY_NAME = "Bolt";
	public final static String PERM_NODE = "bolt";
	
	/**
	 * Constructs a new BoltAbility instance with the specified player as the ability user
	 * 
	 * @param player - The player to use as the abilities user
	 */
	public BoltAbility(Player player) {
		this(player, 10);
	}
	
	/**
	 * Constructs a new BoltAbility instance with the specified player as the ability user and specified range
	 * 
	 * @param player - The player to use as the abilities user
	 * @param range - The max distance away from the user that a target can be
	 */
	public BoltAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
		
		setDamage(8);
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		LivingEntity t = getTarget();
		
		t.getWorld().strikeLightningEffect(t.getLocation());
		t.damage(getDamage(), p);
		
		return true;
	}
	
}
