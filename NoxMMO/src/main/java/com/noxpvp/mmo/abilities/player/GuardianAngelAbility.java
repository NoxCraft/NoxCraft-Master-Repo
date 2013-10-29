package com.noxpvp.mmo.abilities.player;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.NumberConversions;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.DespawnRunnable;
import com.noxpvp.mmo.runnables.EffectRunnable;
import com.noxpvp.mmo.runnables.EntityEffectRunnable;
import com.noxpvp.mmo.runnables.HealRunnable;

public class GuardianAngelAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Guardian Angel";
	private int range = 5;
	private double healAmount = 6;
	private Effect leaveEffect;
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Integer - Currently set range for the angel heal range
	 */
	public int getRange() {return range;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param range - The Integer range the angel should look for player to heal
	 * @return GuardianAngelAbility - This instance, used for chaining
	 */
	public GuardianAngelAbility setRange(int range) {this.range = range; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Integer - The currently set amount to heal players
	 */
	public double getHealAmount() {return healAmount;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param healAmount - double amount that the angel should heal players
	 * @return GuardianAngelAbility - This instance, used for chaining
	 */
	public GuardianAngelAbility setHealAmount(double healAmount) {this.healAmount = healAmount; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Effect - The current effect set for when the guardian angel vanishes / is removed
	 */
	public Effect getLeaveEffect() {return this.leaveEffect;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param leaveEffect - Effect type for the angel when removed / leaves after healing is complete
	 * @return guardianAngelAbility - This instance, used for chaining
	 */
	public GuardianAngelAbility setLeaveEffect(Effect leaveEffect) {this.leaveEffect = leaveEffect; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public GuardianAngelAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Boolean - If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
		
		NoxMMO instance = NoxMMO.getInstance();
		
		for (Entity it : v.getNearbyEntities(range, range, range)){
			if (!(it instanceof Player)) continue;
			
			LivingEntity e = (LivingEntity) it;
			
			HealRunnable heal = new HealRunnable(e, getHealAmount(), 1);
			EntityEffectRunnable effect = new EntityEffectRunnable(e, EntityEffect.WOLF_HEARTS, NumberConversions.toInt((getHealAmount() / 2)));
			
			heal.runTaskLater(instance, 40);
			effect.runTaskTimer(instance, 25, 5);
		}
		EffectRunnable leaveEffect = new EffectRunnable(v, getLeaveEffect(), 1, 5);
		DespawnRunnable despawn = new DespawnRunnable(v);
		leaveEffect.runTaskTimer(instance, 90, 2);
		despawn.runTaskLater(instance, 100);
		
		return true;
	}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Boolean - If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}
	
}
