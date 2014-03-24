package com.noxpvp.mmo.abilities.targeted;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.core.utils.EffectsRunnable;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class SootheAbility extends BaseTargetedPlayerAbility{
	
	public final static String ABILITY_NAME = "Soothe";
	public final static String PERM_NODE = "soothe";
	
	private double healAmount;
	
	/**
	 * Gets the amount of health that will be given to the target
	 * 
	 * @return Double The amount to heal the target
	 */
	public double getHealAmount() {return healAmount;}
	
	/**
	 * Sets the amount of health to give to the target
	 * 
	 * @param healAmount The amount to heal the target
	 * @return SootheAbility This instance
	 */
	public SootheAbility setHealAmount(double healAmount) {this.healAmount = healAmount; return this;}
	
	/**
	 * Constructs a new Soothe Ability with the provided player as the user
	 * 
	 * @param player The ability's user
	 */
	public SootheAbility(Player player) {
		super(ABILITY_NAME, player, PlayerManager.getInstance().getPlayer(player).getTarget());
		
		this.healAmount = 8;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		LivingEntity t = getTarget();
		Location tLoc = t.getLocation(), loc = new Location(tLoc.getWorld(), tLoc.getX(), tLoc.getY()+1.75, tLoc.getZ());
		
		t.setHealth(t.getHealth() + getHealAmount());
		new EffectsRunnable(Arrays.asList("heart"), false, loc, 0, 1, (int) getHealAmount() / 2, null).runTaskTimer(NoxMMO.getInstance(), 0, 6);
		
		return false;
	}

}
