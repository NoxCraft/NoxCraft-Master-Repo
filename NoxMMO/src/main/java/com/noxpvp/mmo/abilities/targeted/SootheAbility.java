package com.noxpvp.mmo.abilities.targeted;

import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
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
		super(ABILITY_NAME, player, NoxMMO.getInstance().getPlayerManager().getMMOPlayer(player).getTarget());
		
		this.healAmount = 8;
	}
	
	/**
	 * Returns if the ability execution was carried out successfully
	 * 
	 * @return boolean If this ability executed successfully
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		LivingEntity t = getTarget();
		
		t.setHealth(t.getHealth() + getHealAmount());
		t.playEffect(EntityEffect.WOLF_HEARTS);
		
		return false;
	}

}
